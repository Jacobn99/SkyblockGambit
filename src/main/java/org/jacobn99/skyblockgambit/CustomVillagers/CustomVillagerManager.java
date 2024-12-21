package org.jacobn99.skyblockgambit.CustomVillagers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.A;
import org.jacobn99.skyblockgambit.GameManager;

import java.util.*;

public class CustomVillagerManager {
    List<CustomVillager> _customs;
    JavaPlugin _mainPlugin;
    GameManager _gameManager;
    Set<String> _presets;
//    int _presetCount;
    public CustomVillagerManager(JavaPlugin mainplugin, List<CustomVillager> customs, GameManager gameManger) {
        _mainPlugin = mainplugin;
        _customs = customs;
        _gameManager = gameManger;
//        String[] presets = {"Villager0", "Villager1", "Villager2"};
        _presets = _mainPlugin.getConfig().getValues(false).keySet();
//        _presets.addAll(Arrays.asList(presets));
    }

    public Villager SpawnVillager(Location loc, Villager.Profession profession) {
        // Spawn a villager with all trades unlocked
        Villager villager = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        villager.setCustomNameVisible(true);
        villager.setProfession(profession); // Set the villager's profession (optional)
        villager.setVillagerExperience(5000); // Set the villager's experience to the maximum
//        villager.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 100, true));
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
                //Bukkit.broadcastMessage("Old ingredient: " + ingredient.getType().name() + ", Amount: " + amount);

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
            //Bukkit.broadcastMessage("Old ingredient: " + ingredient.getType().name() + ", Amount: " + amount);

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
        professionID = allowedProfessions.get(rand.nextInt(allowedProfessions.size()));
        return professionID;
    }

    public CustomVillager CreateCustomVillager(String preset, Location loc, Villager.Profession profession) {
        CustomVillager custom = new CustomVillager(_mainPlugin,
                SpawnVillager(loc, profession), _customs, -1);
        Villager villager = custom.GetVillager();

        if(preset != null) {
            Bukkit.broadcastMessage(preset);
            custom.SetTrades(preset);
            custom.GetVillager().setCustomName(preset);
        }
        villager.setVillagerLevel(5);
        villager.addScoreboardTag("Customized");
        villager.setCustomNameVisible(true);
//        villager.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 100, true));
        _customs.add(custom);
        _gameManager.disposableEntities.add(custom.GetVillager());
        return custom;
    }

    public void VillagerDeathCheck(EntityDeathEvent event) {
        if(_gameManager.isRunning && event.getEntity() instanceof Villager
                && event.getEntity().getScoreboardTags().contains("custom")) {
            CustomVillager customVil = GetFromCustoms((Villager) event.getEntity());
            if(customVil != null) {
                Villager villager = (Villager) event.getEntity();
                CustomVillager newCustom = CreateCustomVillager(null, customVil.GetSpawnLocation(), villager.getProfession());
                ApplyTraits(customVil.GetVillager(), newCustom.GetVillager());
//                newCustom.GetVillager().setRecipes(customVil.GetVillager().getRecipes());
////
//                for(String tag : event.getEntity().getScoreboardTags()) {
//                    newCustom.GetVillager().addScoreboardTag(tag);
//                }
            }
        }
    }
    public void ApplyTraits(Villager copied, Villager pasted) {
        pasted.setRecipes(copied.getRecipes());
        for(String tag : copied.getScoreboardTags()) {
            pasted.addScoreboardTag(tag);
        }
        pasted.setProfession(copied.getProfession());
        pasted.setCustomName(copied.getCustomName());
        pasted.setCustomNameVisible(true);

    }

    public CustomVillager GetFromCustoms(Villager v) {
        for (CustomVillager c : get_customs()) {
            if (c.GetVillager() == v) {
                return c;
            }
        }
        return null;
    }

    public List<CustomVillager> get_customs() {
        return _customs;
    }

    public void set_customs(List<CustomVillager> _customs) {
        this._customs = _customs;
    }

    public Set<String> get_presets() {
        return _presets;
    }

    public void set_presets(Set<String> _presets) {
        this._presets = _presets;
    }
}
