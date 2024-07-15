package org.jacobn99.skyblockgambit;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public class CustomVillagerManager {
    List<CustomVillager> _customs;
    List<Entity> _disposableEntities;
    JavaPlugin _mainPlugin;
    CustomVillagerManager(JavaPlugin mainplugin, List<CustomVillager> customs, List<Entity> disposableEntities) {
        _mainPlugin = mainplugin;
        _customs = customs;
        _disposableEntities = disposableEntities;
    }
    //    public void SpawnTeamVillagers() {
//        Location spawnLoc = GetBlueSpawn();
//
//        for(int i = 0; i < 2; i++) {
//            SpawnVillager(spawnLoc, Villager.Profession.CLERIC);
//            SpawnVillager(spawnLoc, Villager.Profession.FARMER);
//            SpawnVillager(spawnLoc, Villager.Profession.ARMORER);
//            SpawnVillager(spawnLoc, Villager.Profession.BUTCHER);
//            SpawnVillager(spawnLoc, Villager.Profession.CARTOGRAPHER);
//            SpawnVillager(spawnLoc, Villager.Profession.FISHERMAN);
//            SpawnVillager(spawnLoc, Villager.Profession.FLETCHER);
//            SpawnVillager(spawnLoc, Villager.Profession.LIBRARIAN);
//            SpawnVillager(spawnLoc, Villager.Profession.MASON);
//            SpawnVillager(spawnLoc, Villager.Profession.SHEPHERD);
//            SpawnVillager(spawnLoc, Villager.Profession.TOOLSMITH);
//            SpawnVillager(spawnLoc, Villager.Profession.WEAPONSMITH);
//
//            CreateCustomVillager("Villager0", spawnLoc, Villager.Profession.NITWIT);
//            CreateCustomVillager("Villager1", spawnLoc, Villager.Profession.NITWIT);
//
//            spawnLoc = GetRedSpawn();
//        }
//    }

    public Villager SpawnVillager(Location loc, Villager.Profession profession) {
        // Spawn a villager with all trades unlocked
        Villager villager = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        villager.setProfession(profession); // Set the villager's profession (optional)
        villager.setVillagerExperience(5000); // Set the villager's experience to the maximum
        _disposableEntities.add(villager);
        return villager;
    }

    public Villager.Profession SetRandomProfession() {
        Random rand = new Random();
        int professionID;
        professionID = rand.nextInt(15);

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
        _customs.add(custom);
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
