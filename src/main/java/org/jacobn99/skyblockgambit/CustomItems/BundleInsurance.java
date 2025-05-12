package org.jacobn99.skyblockgambit.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;

import java.util.*;

public class BundleInsurance {
    private GameManager _gameManager;
    private ItemStack _template;
    private CustomItemManager _itemManager;
    public Map<Player, ItemStack> inventories;
    ProcessManager _processManager;
    public BundleInsurance(GameManager gameManager, CustomItemManager itemManager) {
        _gameManager = gameManager;
        _itemManager = itemManager;
        _template = itemManager.GetCustomItem(itemManager.ItemNameToIndex("BUNDLE_INSURANCE"));
        inventories = new HashMap<>();
        _processManager = _gameManager._processManager;
    }
    public boolean isBundleInsurance(ItemStack bundle) {
        return bundle != null && bundle.getType() == _template.getType() &&
                bundle.getItemMeta().getLore().size() > 0 &&
                bundle.getItemMeta().getLore().contains(_template.getItemMeta().getLore().get(0));
    }
    public boolean isUnownedBundleInsurance(ItemStack bundle) {
        return isBundleInsurance(bundle) && bundle.getItemMeta().getLore().size() < 2;
    }

    public Player GetOwner(ItemStack bundle) {
        List<String> lore = bundle.getItemMeta().getLore();
        if(bundle.getType() != _template.getType()) Bukkit.broadcastMessage("ERROR1: not of type bundle");
        else if(lore.size() < 1) Bukkit.broadcastMessage("ERROR: BUNDLE WITH NO LORE");
        else if(lore.size() < 2) return null;
        else return Bukkit.getPlayer(lore.get(1).substring(7));
        return null;
    }

    public ItemStack SetOwner(Player p, ItemStack bundle){
        if(!isBundleInsurance(bundle)) Bukkit.broadcastMessage("ERROR: not bundle insurance");
        else {
//            if(inventories.containsKey(p)) p.sendMessage("Player already has bundle insurance");
            ItemStack newBundle = bundle.clone();
            ItemMeta meta = newBundle.getItemMeta();
            List<String> lore = meta.getLore();
            if(lore.size() == 1) lore.add("");
            lore.set(1, "Owner: " + p.getName());
            meta.setLore(lore);
            newBundle.setItemMeta(meta);

            inventories.put(p, bundle);
            return newBundle;
        }
        return bundle;
    }

    public void UpdateMap(Player p, ItemStack bundle) {
        if(bundle == null || bundle.getType() != _template.getType())
            Bukkit.broadcastMessage("ERROR2: not of type bundle");
        else {
            if(!inventories.containsKey(p)) inventories.put(p, bundle);
            else inventories.replace(p, bundle);
        }
    }
    public void SafeUpdateMap(Player p, ItemStack bundle, int slot) {
        ItemStack slotItem = p.getInventory().getItem(slot);
        if(!isBundleInsurance(slotItem)) UpdateMap(p, bundle);
        else UpdateMap(p, p.getInventory().getItem(slot));
    }
    public ItemStack GetEmptyBundle(Player owner) {
        ItemStack bundle = _template.clone();
        bundle = SetOwner(owner, bundle);
        return bundle;
    }
    public void ReplaceUnassignedBundle(Player owner, int slot) {
        if(!inventories.containsKey(owner)) {
            ItemStack bundle = owner.getInventory().getItem(slot);
            bundle = SetOwner(owner, bundle);
            owner.getInventory().setItem(slot, bundle);
            UpdateMap(owner, bundle);
        }
        else {
            owner.getInventory().setItem(slot, inventories.get(owner));
        }
    }
    public void GrantOwnedBundle(Player owner) {
        if(inventories.containsKey(owner)) owner.getInventory().addItem(inventories.get(owner));
        else {
            ItemStack bundle = SetOwner(owner, _template.clone());
            owner.getInventory().addItem(bundle);
        }
    }
    public ItemStack GetNormalBundle(ItemStack bundle) {
        ItemStack newBundle = bundle.clone();
        ItemMeta meta = newBundle.getItemMeta();

        List<String> newLore = new ArrayList<>();
        newLore.add("This was someone's bundle insurance");
        meta.setLore(newLore);
        newBundle.setItemMeta(meta);
        return newBundle;
    }
    public void ResetBundleInsurance(Player p) {
        if(inventories.containsKey(p)) {
            inventories.replace(p, GetEmptyBundle(p));
        }
    }
    public void DeathCheck(Player victim, boolean isPvpDeath) {
        Collection<Entity> entities = victim.getWorld().getNearbyEntities(victim.getLocation(), 10, 10, 10);

        int count = 0;
        for(Entity e : entities) {
            if (e.getType() == EntityType.DROPPED_ITEM) {
                Item item = (Item) e;
                ItemStack stack = item.getItemStack();
                if (isBundleInsurance(stack)) {
                    Player o = GetOwner(stack);
                    if (o == victim) {
                        if (count < 1 && isPvpDeath) item.setItemStack(GetNormalBundle(stack));
                        else item.setItemStack(new ItemStack(Material.LEATHER));
                        count += 1;
                    } else if (o != null) item.setItemStack(new ItemStack(Material.LEATHER));
                }
            }
        }
    }
    public boolean HasDuplicates(Player owner) {
        Inventory inventory = owner.getInventory();
        int count = 0;
//        if(isHoldingBundle) count = 1;
        for(int i = 0; i < inventory.getSize(); i++) {
//            Bukkit.broadcastMessage("count: " + count);
            ItemStack item = inventory.getItem(i);
            if(isBundleInsurance(item)) {
                Player o = GetOwner(item);
                if(o == owner) count +=1;
                if(count > 1) return true;

            }
        }
        return false;
    }

    public void BundleCheck(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if(isUnownedBundleInsurance(item) &&
                (event.getAction().equals(Action.RIGHT_CLICK_AIR) ||
                event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) &&
                event.getHand().equals(EquipmentSlot.HAND)) {
            ReplaceUnassignedBundle(p, p.getInventory().getHeldItemSlot());
            event.setCancelled(true);

        }
        else if(isBundleInsurance(item)) {
            Player owner = GetOwner(item);
            if((owner != null && owner != p) || HasDuplicates(p))  {
                event.setCancelled(true);
                p.getInventory().setItemInMainHand(new ItemStack(Material.BUNDLE));
            }
        }
    }
}
