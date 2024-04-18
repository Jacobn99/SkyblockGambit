package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        int loopLimit = 20;
        List<String> customItemNames = new ArrayList();
        List<MerchantRecipe> newRecipes = new ArrayList<>();
        List<String> args;


        //Make a for loop to see if any of the arguments are part of the special item name list, if they are change their arg index to the material of the item they represent
        for(int i = 0; i < loopLimit; i++) {
            args = _configManager.GetArguments(villagerPath, "Trades", "Trade" + i);
            for(CustomItems ci : _configManager.GetCustomItemsList()) {
                if(!customItemNames.contains(ci.getItemName())) {
                    customItemNames.add(ci.getItemName());
                }
                //Bukkit.broadcastMessage("customItemNames: " + customItemNames);
            }

            if(args != null) {
                int lastIndex = args.size() - 1;
                ItemStack product;

                //Bukkit.broadcastMessage("list: " + customItemNames + ", arg: " + args.get(lastIndex - 1));

                if(customItemNames.contains(args.get(lastIndex - 1))) {
                    product = _configManager.GetCustomItem(_configManager.ItemNameToIndex(args.get(lastIndex - 1)));
                    product.setAmount(Integer.parseInt(args.get(lastIndex)));
                }
                else {
                    product = new ItemStack(Material.valueOf(args.get(lastIndex - 1)), Integer.parseInt(args.get(lastIndex)));
                }

                MerchantRecipe recipe = new MerchantRecipe(product, 10000);

                for(int ii = 0; ii < (args.size() - 3); ii += 2) {
                    ItemStack ingredient;
                    if(customItemNames.contains(args.get(ii))) {
                        ingredient = _configManager.GetCustomItem(_configManager.ItemNameToIndex(args.get(ii)));
                        ingredient.setAmount(Integer.parseInt(args.get(ii + 1)));
                    }
                    else {
                        ingredient = new ItemStack(Material.valueOf(args.get(ii)), Integer.parseInt(args.get(ii + 1)));
                    }

                    recipe.addIngredient(ingredient);
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
    }

    public void SetVillager(Villager v) {
        _villager = v;
    }

    public Villager getVillager() {
        return _villager;
    }
}
