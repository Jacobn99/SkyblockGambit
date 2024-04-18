package org.jacobn99.skyblockgambit;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class EventManager implements Listener {
    JavaPlugin _mainPlugin;
    GameManager _gameManager = new GameManager(_mainPlugin);
    Borderwall _borderwall = new Borderwall(_mainPlugin);
    public EventManager(JavaPlugin mainPlugin) {
        _mainPlugin = mainPlugin;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        Location borderLocation = _borderwall.GetBorderLocation();
        double blockZ = block.getLocation().getZ();

        if (blockZ >= borderLocation.getZ() - 0.5 && blockZ <= borderLocation.getZ() + 0.5) {
            event.setCancelled(true);
        }
    }
}
