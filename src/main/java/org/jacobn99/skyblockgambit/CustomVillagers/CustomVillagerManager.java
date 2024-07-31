package org.jacobn99.skyblockgambit.CustomVillagers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jacobn99.skyblockgambit.GameManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomVillagerManager {
    List<CustomVillager> _customs;
    //List<Entity> _disposableEntities;
    JavaPlugin _mainPlugin;
    GameManager _gameManager;
    public CustomVillagerManager(JavaPlugin mainplugin, List<CustomVillager> customs, GameManager gameManger) {
        _mainPlugin = mainplugin;
        _customs = customs;
        _gameManager = gameManger;
        //_disposableEntities = disposableEntities;
    }

    public Villager SpawnVillager(Location loc, Villager.Profession profession) {
        // Spawn a villager with all trades unlocked
        Villager villager = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        villager.setProfession(profession); // Set the villager's profession (optional)
        villager.setVillagerExperience(5000); // Set the villager's experience to the maximum
        villager.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 100, true));
        _gameManager.disposableEntities.add(villager);
        return villager;
    }
    public void MakeTradesCheaper(Villager v) {
        List<MerchantRecipe> newRecipes = new ArrayList<>();

        for(MerchantRecipe recipe : v.getRecipes()) {
            List<ItemStack> newIngredients = new ArrayList<>();
            for(ItemStack ingredient : recipe.getIngredients()) {
                int amount = ingredient.getAmount();
                int newAmount = 1;
                ItemStack newIngredient;
                Bukkit.broadcastMessage("Old ingredient: " + ingredient.getType().name() + ", Amount: " + amount);

                if(amount > 1) {
                    newAmount = Math.round((amount/2));
                }
                newIngredient = new ItemStack(ingredient.getType(), newAmount);
                newIngredients.add(newIngredient);
                //Bukkit.broadcastMessage("New ingredient: " + ingredient.getType().name() + ", Amount: " + newAmount);

            }
            recipe.setIngredients(newIngredients);
            newRecipes.add(recipe);
        }
    }
    public MerchantRecipe MakeTradeCheaper(MerchantRecipe recipe) {
        List<ItemStack> newIngredients = new ArrayList<>();

        for(ItemStack ingredient : recipe.getIngredients()) {
            int amount = ingredient.getAmount();
            int newAmount = 1;
            ItemStack newIngredient;
            Bukkit.broadcastMessage("Old ingredient: " + ingredient.getType().name() + ", Amount: " + amount);

            if(amount > 1) {
                newAmount = Math.round((amount/2));
            }
            newIngredient = new ItemStack(ingredient.getType(), newAmount);
            newIngredients.add(newIngredient);
            //Bukkit.broadcastMessage("New ingredient: " + ingredient.getType().name() + ", Amount: " + newAmount);

        }
        recipe.setIngredients(newIngredients);
        return recipe;
    }


    public Integer GetRandomProfessionID(List<Integer> bannedProfessionIDs) {
        Random rand = new Random();
        int professionID;

        List<Integer> allowedProfessions = new ArrayList<>();
        for(int e = 0; e < 14; e++) {
            if(!bannedProfessionIDs.contains(e)) {
                allowedProfessions.add(e);
            }
        }

//        for(int i : bannedProfessionIDs) {
//            allowedProfessions.remove(i);
//        }
        professionID = allowedProfessions.get(rand.nextInt(allowedProfessions.size()));

        //Villager.Profession profession = Villager.Profession.values()[professionID];
        return professionID;
        //_villager.setProfession(profession);
    }

    public CustomVillager CreateCustomVillager(String preset, Location loc, Villager.Profession profession) {
        CustomVillager custom = new CustomVillager(_mainPlugin,
                SpawnVillager(loc, profession), _customs, -1);
        Villager villager = custom.GetVillager();

        if(preset != null) {
            custom.SetTrades(preset);
        }
        villager.setVillagerLevel(5);
        villager.addScoreboardTag("Customized");
        villager.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 100, true));
        _customs.add(custom);
        _gameManager.disposableEntities.add(custom.GetVillager());
        return custom;
    }
//    public CustomVillager CreateCustomVillager(String preset, Location loc, Villager.Profession profession) {
//        CustomVillager custom = new CustomVillager(_mainPlugin,
//                SpawnVillager(loc, profession));
//        custom.SetTrades(preset);
//        custom.getVillager().setVillagerLevel(5);
//        customVillagers.add(custom);
//        return custom;
//    }
}
