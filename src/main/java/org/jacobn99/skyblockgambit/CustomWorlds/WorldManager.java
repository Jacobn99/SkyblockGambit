package org.jacobn99.skyblockgambit.CustomWorlds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomVillagers.CustomVillager;
import org.jacobn99.skyblockgambit.CustomVillagers.CustomVillagerManager;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Portals.Portal;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.Processes.Process;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;
import org.jacobn99.skyblockgambit.StarterChest.StarterChest;
import org.jacobn99.skyblockgambit.StarterChest.StarterChestManager;
import org.jacobn99.skyblockgambit.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WorldManager {
    private Random rand;
    private List<CustomWorld> _customWorlds;
    public WorldCopier _worldCopier;
    private JavaPlugin _mainPlugin;
    private int _worldLength;
    private GameManager _gameManager;
    private PortalManager _portalManager;
    private ProcessManager _processManager;
    private CustomVillagerManager _villagerManager;
    final private int _pieceHeight;
    final private int _pieceLength;
    final private int _chunkWorldSize;

    public WorldManager(JavaPlugin mainPlugin, GameManager gameManager, PortalManager portalManager, ProcessManager processManager, CustomVillagerManager customVillagerManager) {
        rand = new Random();
        _mainPlugin = mainPlugin;

        _pieceHeight = 100;
        _pieceLength = 16;
        _chunkWorldSize = 3;
        // USE THIS IF GOING BACK TO NATURAL GEN
//        _worldLength = _chunkWorldSize * _pieceLength;
        _worldLength = 176;


        _gameManager = gameManager;
        _portalManager = portalManager;
        _processManager = processManager;
        _worldCopier = new WorldCopier(_mainPlugin, _gameManager.processes, _processManager);
        _customWorlds = _gameManager.customWorlds;
        _villagerManager = customVillagerManager;
    }
    public void ClearWorlds() {
        for(CustomWorld customWorld : _customWorlds) {
            _processManager.CreateProcess(_processManager.GetLatestExecutionTime() + 30, () -> _worldCopier.ClearWorld(customWorld.GetMiddleLoc(), _worldLength));
//            _processManager.CreateProcess(_processManager.GetLatestExecutionTime() + 30, () ->_processManager.CreateProcess(_processes, _processManager.GetLatestExecutionTime(_processes) + 30, () -> _worldCopier.ClearWorld(customWorld.GetMiddleLoc(), _worldLength)));
        }
    }
    public Location GenerateSpawnLocation(World world, Location middleLocation, int maxY, int minY, int spawnRadius) {
        int x;
        int z;
        for(int i = 0; i < 70; i++) {
            x = rand.nextInt(spawnRadius*2);
            z = rand.nextInt(spawnRadius*2);

//            Bukkit.broadcastMessage("max: " + (middleLocation.getZ() + spawnRadius*2 - (double) spawnRadius) + ", radius: " + spawnRadius + ", middleZ: " + middleLocation.getZ());
//            Bukkit.broadcastMessage("min: " + (middleLocation.getZ() + 0 - (double) spawnRadius) + ", radius: " + spawnRadius + ", middleZ: " + middleLocation.getZ());

            Location loc = new Location(world, middleLocation.getX() + x - (double) spawnRadius, middleLocation.getY(), middleLocation.getZ() + z - (double) spawnRadius);
            loc = _gameManager.FindSurface(loc, maxY, minY);

            if(loc != null) {
                return loc;
            }
        }
        Bukkit.broadcastMessage("Loc was null");
        return null;
    }
    public void AddPostGenerationObjects(StarterChestManager _chestManager, CustomVillagerManager villagerManager, List<CustomVillager> customs) {
//        try {

        Bukkit.broadcastMessage("World has been generated");
        Bukkit.broadcastMessage("post gen time: " + Bukkit.getWorld("void_world").getFullTime());
        _gameManager.isWorldGenerated = true;

        SpawnStarterChests(_chestManager);
        SpawnPortals();
        SpawnTeamVillagers(villagerManager);
        _gameManager.InitializeTeams();
        _gameManager.UpdateSpawns();
//            _gameManager._animalSpawner.SpawnAnimals(false);
//         }
//        catch(Exception e) {
//            Bukkit.broadcastMessage("ERROR");
//            e.printStackTrace();
//        }
    }
    public void SpawnTeamVillagers(CustomVillagerManager villagerManager) {
        List<CustomVillager> templateVillagers = new ArrayList<>();
        int extraVillagers = _villagerManager.get_presets().size() + 1;
        List<Integer> bannedProfessions = new ArrayList<>();
        bannedProfessions.add(5); //Farmer
        bannedProfessions.add(11); //Nitwit
        bannedProfessions.add(0); //None
        bannedProfessions.add(3); //Cartographer

        int iterations = 0;
        for(CustomWorld customWorld : _customWorlds) {
//            Bukkit.broadcastMessage("customs size: " + customs.size());
            Location spawnLoc = customWorld.GetWorldSpawn(_gameManager);


            if(iterations == 0) {
                for (int i = 0; i < _gameManager.normalVillagerAmount - extraVillagers; i++) {
                    //Location spawnLoc = GenerateSpawnLocation(refreneceLoc, spawnRadius);
                    if(bannedProfessions.size() == 15) {
                        bannedProfessions.clear();
                        bannedProfessions.add(5); //Farmer
                        bannedProfessions.add(11); //Nitwit
                        bannedProfessions.add(0); //None
                        bannedProfessions.add(3); //Cartographer
                    }
                    int professionID = villagerManager.GetRandomProfessionID(bannedProfessions);
                    Villager.Profession profession = Villager.Profession.values()[professionID];
                    bannedProfessions.add(professionID);
                    Villager vil = villagerManager.SpawnVillager(spawnLoc, profession);
                    _villagerManager.MakeTradesCheaper(vil);
                    CustomVillager customVillager = new CustomVillager(_mainPlugin, vil, _gameManager.getCustomVillagers(), i);
                    templateVillagers.add(customVillager);
                }

                CustomVillager farmer = _villagerManager.CreateCustomVillager(null, spawnLoc, Villager.Profession.FARMER);
                templateVillagers.add(farmer);
                for(String preset : _villagerManager.get_presets()) {
                    CustomVillager c = _villagerManager.CreateCustomVillager(preset, spawnLoc, Villager.Profession.NITWIT);
                    templateVillagers.add(c);
                }
//
//                _villagerManager.CreateCustomVillager("Villager0", GenerateSpawnLocation(world, spawnLoc,300, _gameManager.minWorldHeight, spawnRadius), Villager.Profession.NITWIT);
//                _villagerManager.CreateCustomVillager("Villager1", GenerateSpawnLocation(world, spawnLoc,300, _gameManager.minWorldHeight, spawnRadius), Villager.Profession.NITWIT);
//                _villagerManager.CreateCustomVillager("Villager2", GenerateSpawnLocation(world, spawnLoc,300, _gameManager.minWorldHeight, spawnRadius), Villager.Profession.NITWIT);

//                templateVillagers.addAll(_villagerManager.get_customs());
            }
            else {
                int i = 0;
                for(CustomVillager customVillager : templateVillagers) {
                    Villager villager = _villagerManager.SpawnVillager(spawnLoc, customVillager.GetVillager().getProfession());
                    villager.setRecipes(templateVillagers.get(i).GetVillager().getRecipes());

                    CustomVillager customVil = new CustomVillager(_mainPlugin, villager, _gameManager.getCustomVillagers(), customVillager.GetID());
                    villager.addScoreboardTag("villager" + iterations);
                    i++;
                }
            }
            iterations++;
        }
    }

    public void SpawnStarterChests(StarterChestManager chestManager) {
        for(CustomWorld customWorld : _customWorlds) {
            Location worldSpawn = customWorld.GetWorldSpawn(_gameManager);
            Location startChestLoc = new Location(Bukkit.getWorld("void_world"), worldSpawn.getX() + 1, worldSpawn.getY(), worldSpawn.getZ() + 1);
            StarterChest starterChest = new StarterChest(startChestLoc, chestManager.GetInventory(), _gameManager.GetStarterChestList());
            starterChest.CreateChest();
        }
    }
    public void SpawnPortals() {
        Location portalLoc;
        CustomWorld currentOpposingWorld;

        int i = 0;
        for(CustomWorld customWorld : _customWorlds) {
            Location worldSpawn = customWorld.GetWorldSpawn(_gameManager);
            portalLoc = new Location(worldSpawn.getWorld(), worldSpawn.getX() + 5, 0, worldSpawn.getZ() + 5);
            portalLoc = _gameManager.FindSurface(portalLoc, 300, _gameManager.minWorldHeight);

            if (portalLoc == null) {
                portalLoc = customWorld.GetWorldSpawn(_gameManager).clone().add(0, 5, 0);
            }

            if(i < _customWorlds.size() - 1) {
                currentOpposingWorld = _customWorlds.get(i + 1);
            }
            else if (_customWorlds.size() > 1) {
                currentOpposingWorld = _customWorlds.get(i - 1);
            }
            else {
                Bukkit.broadcastMessage("ERROR: NO CUSTOM WORLD");
                return;
            }
            _gameManager.GenerateInvaderPortalFrame(portalLoc);
            Portal p = new Portal(_gameManager.portals, _portalManager,
                    currentOpposingWorld.GetWorldSpawn(_gameManager), portalLoc);
            customWorld.SetWorldPortal(p);

            ArmorStand armorStand = (ArmorStand) portalLoc.getWorld().spawnEntity(portalLoc,
                    EntityType.ARMOR_STAND);
            armorStand.setGlowing(true);
            armorStand.setGravity(false);
            armorStand.addScoreboardTag("disposable");

            portalLoc = null;
            worldSpawn = null;
            i++;
        }
    }
    public void BuildWorld(CustomWorld newWorld, File worldFile, ProcessManager processManager) {
        long executionTime = processManager.GetLatestExecutionTime() + 10;
        Queueable queueable = () -> _worldCopier.DuplicateLand(newWorld.GetMiddleLoc(), _worldLength, worldFile);
        _processManager.CreateProcess(executionTime, queueable);
    }
    public int get_worldLength() {
        return _worldLength;
    }
    public int get_pieceHeight() {
        return _pieceHeight;
    }
    public int get_pieceLength() {
        return _pieceLength;
    }

    public int get_chunkWorldSize() {
        return _chunkWorldSize;
    }
}