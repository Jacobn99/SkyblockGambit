package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CustomVillager {
    List<MerchantRecipe> recipes;
    ConfigManager _configManager;
    JavaPlugin _mainPlugin;
    Villager _villager;
    public CustomVillager(JavaPlugin mainPlugin, Villager villager) {
        recipes = new ArrayList<>();
        _mainPlugin = mainPlugin;
        _configManager = new ConfigManager(_mainPlugin);
        _villager = villager;
    }
    public void SetTrades(String villagerPath) {
        List<MerchantRecipe> newRecipes = new ArrayList<>();
        List<String> args = new ArrayList<>();
        ConfigurationSection section = _mainPlugin.getConfig().getConfigurationSection(villagerPath)
                .getConfigurationSection("Trades");
        for(int i = 0; i < 10; i++) {
            args = _configManager.GetArguments(villagerPath, "Trades", "Trade" + i);

            if(args != null) {
                int lastIndex = args.size() - 1;
                ItemStack item = new ItemStack(Material.valueOf(args.get(lastIndex - 1)), Integer.parseInt(args.get(lastIndex)));
                MerchantRecipe recipe = new MerchantRecipe(item, 10000);
                recipe.addIngredient(new ItemStack(Material.valueOf(args.get(0)), Integer.parseInt(args.get(1))));

                if (args.size() == 6) {
                    recipe.addIngredient(new ItemStack(Material.valueOf(args.get(2)), Integer.parseInt(args.get(3))));
                }
                newRecipes.add(recipe);
            }
            else {
                break;
            }
        }
        if(!newRecipes.isEmpty()) {
            _villager.setRecipes(newRecipes);
        }

        //MerchantRecipe firstRecipe = new MerchantRecipe(_configManager.GetArguments("Villager1", "Trades"), 10000);
//
//        firstRecipe.addIngredient(new ItemStack(Material.EMERALD));
//
//        newRecipes.add(firstRecipe);
//        merchant.setRecipes(newRecipes);
    }

    public void SetVillager(Villager v) {
        _villager = v;
    }

    public Villager getVillager() {
        return _villager;
    }
}
