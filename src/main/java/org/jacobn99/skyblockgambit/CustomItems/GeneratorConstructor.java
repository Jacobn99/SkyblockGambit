package org.jacobn99.skyblockgambit.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jacobn99.skyblockgambit.GeneratorInfo.GeneratorManager;
import org.jacobn99.skyblockgambit.GeneratorInfo.ItemGenerator;

import java.util.ArrayList;
import java.util.List;

public class GeneratorConstructor {
    private List<ItemGenerator> _generators;
    private CustomItemManager _itemManager;
    private GeneratorManager _generateManager;
    Inventory generatorSelector;
    public GeneratorConstructor(List<ItemGenerator> generators, GeneratorManager generatorManager, CustomItemManager itemManager) {
        _generators = generators;
        _itemManager = itemManager;
        _generateManager = generatorManager;
        InitializeSelectorInventory();
    }
    public void InitializeSelectorInventory() {
        generatorSelector = Bukkit.createInventory(null, 9, "Select a generator!");
        generatorSelector.setItem(0, GenerateGeneratorSymbol(Material.DIAMOND));
    }
    private ItemStack GenerateGeneratorSymbol(Material material) {
        String itemName;
        ItemStack item = null;
        ItemGenerator generator = _generateManager.GetGeneratorType(material);
        if(generator != null) {
            item = new ItemStack(material);
            itemName = item.getType().name().toLowerCase().replace('_', ' ');

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(itemName + " generator");
            List<String> lore = new ArrayList<>();
            lore.add("This generator costs " + generator.GetCost().getAmount() + " " + generator.GetCost().getType().name().toLowerCase().replace('_', ' '));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }
    private boolean HasFunds(Inventory inventory, ItemStack item) {
        int count = 0;
        for (ItemStack i : inventory.getContents()) {
            if (i != null) {
                if (i.getType().equals(item.getType())) {
                    count += i.getAmount();
                }
            }
        }
        Bukkit.broadcastMessage("count: " + count);
        Bukkit.broadcastMessage("target count: " + item.getAmount());

        if(count >= item.getAmount()) {
            return true;
        }
        else {
            return false;
        }
    }
    public void GeneratorConstructorCheck(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if(event.getItem().equals(_itemManager.GetCustomItem(_itemManager.ItemNameToIndex("GENERATOR_CONSTRUCTOR")))) {
            p.openInventory(generatorSelector);
        }
    }
    public void SelectGeneratorCheck(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if(inventory.equals(generatorSelector) && event.getCurrentItem() != null) {
            event.setCancelled(true);
            ItemGenerator generator = _generateManager.GetGeneratorType(event.getCurrentItem().getType());
            if(generator != null) {
                Player p = (Player) event.getWhoClicked();
                if(HasFunds(p.getInventory(), generator.GetCost())) {
                    Bukkit.broadcastMessage("Creating a " + event.getCurrentItem().getType().name().toLowerCase() + " generator!");
                    generator.CreateGenerator(event.getWhoClicked().getLocation());
                    event.getWhoClicked().closeInventory();
                }
                else {
                    Bukkit.broadcastMessage("Not enough funds (poor)");
                }
            }
        }
    }
}
