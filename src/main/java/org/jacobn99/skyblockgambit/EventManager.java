package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.CustomItems.PortalOpener;
import org.jacobn99.skyblockgambit.CustomItems.RageSpell;
import org.jacobn99.skyblockgambit.CustomItems.VillagerTradeBoost;

public class EventManager implements Listener {
    JavaPlugin _mainPlugin;
    CustomItemManager _itemManager;
    Borderwall _borderwall;
    GameManager _gameManager;
    PortalOpener _portalOpener;
    RageSpell _rageSpell;
    VillagerTradeBoost _villagerTradeBoost;
    public EventManager(JavaPlugin mainPlugin, GameManager gameManager) {
        _mainPlugin = mainPlugin;
        _itemManager = new CustomItemManager(_mainPlugin);
        _gameManager = gameManager;
        _borderwall = new Borderwall(_mainPlugin, _gameManager);
        _portalOpener = new PortalOpener(_gameManager);
        _villagerTradeBoost = new VillagerTradeBoost(_gameManager);
        _rageSpell = new RageSpell(_gameManager);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = (Player) event.getEntity();
        _gameManager.UpdateSpawns();
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (_gameManager.isRunning && event.getPlayer().getItemInUse() != null) {
            _portalOpener.PortalOpenerCheck(event, _itemManager);
            _villagerTradeBoost.TradeBoostCheck(event, _itemManager);
            _rageSpell.RageSpellCheck(event, _itemManager);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
//        Villager villagerReference;
//        Villager trader;
        if(event.getInventory() instanceof MerchantInventory) {
            MerchantInventory inventory = (MerchantInventory) event.getInventory();
            Merchant merchant = inventory.getMerchant();
            if(event.getInventory().getHolder() != null)  {
                for(MerchantRecipe recipe : merchant.getRecipes()) {
                    //Bukkit.broadcastMessage("Recipe: " + recipe.getIngredients() + "-> " + recipe.getResult());
                    recipe.setMaxUses(10000);
                }
//                List<MerchantRecipe> newRecipes = new ArrayList<>();
//
//                ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 30);
//
//                MerchantRecipe firstRecipe = new MerchantRecipe(cobblestone, 10000);
//
//                firstRecipe.addIngredient(new ItemStack(Material.EMERALD));
//
//                newRecipes.add(firstRecipe);
//                merchant.setRecipes(newRecipes);

            }
            else {
                return;
            }
        }
    }

//    @EventHandler
//    public void onBlockBreak(BlockBreakEvent event) {
//        Block block = event.getBlock();
//
//        Location borderLocation = _borderwall.GetBorderLocation();
//        double blockZ = block.getLocation().getZ();
//
//        if (blockZ >= borderLocation.getZ() - 0.5 && blockZ <= borderLocation.getZ() + 0.5) {
//            event.setCancelled(true);
//        }
//    }
}
