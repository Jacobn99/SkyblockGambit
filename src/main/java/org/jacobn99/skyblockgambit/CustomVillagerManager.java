package org.jacobn99.skyblockgambit;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomVillagerManager {
    List<CustomVillager> _customs;
    //List<Entity> _disposableEntities;
    JavaPlugin _mainPlugin;
    GameManager _gameManager;
    CustomVillagerManager(JavaPlugin mainplugin, List<CustomVillager> customs, GameManager gameManger) {
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
        _gameManager.disposableEntities.add(villager);
        return villager;
    }

    public Villager.Profession SetRandomProfession() {
        Random rand = new Random();
        int[] allowedProfessions = {0,1,2,3,4,5,6,7,8,9,12,13,14};
        int professionID;
        professionID = allowedProfessions[rand.nextInt(13)];

        Villager.Profession profession;

        profession = Villager.Profession.values()[professionID];
        return profession;
        //_villager.setProfession(profession);
    }

    public CustomVillager CreateCustomVillager(String preset, Location loc, Villager.Profession profession) {
        CustomVillager custom = new CustomVillager(_mainPlugin,
                SpawnVillager(loc, profession), _customs, -1);

        if(preset != null) {
            custom.SetTrades(preset);
        }
        custom.GetVillager().setVillagerLevel(5);
        custom.GetVillager().addScoreboardTag("Customized");
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
