package org.jacobn99.skyblockgambit.CustomWorlds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomVillager;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Portals.Portal;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.Processes.Process;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;
import org.jacobn99.skyblockgambit.StarterChest.StarterChest;
import org.jacobn99.skyblockgambit.StarterChest.StarterChestManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldManager {
    private Random rand;
    private List<CustomWorld> _customWorlds;
    private WorldCopier _worldCopier;
    private JavaPlugin _mainPlugin;
    private int _worldLength;
    private GameManager _gameManager;
    private PortalManager _portalManager;
    private ProcessManager _processManager;
    public WorldManager(JavaPlugin mainPlugin, GameManager gameManager, PortalManager portalManager, ProcessManager processManager) {
        rand = new Random();
        _mainPlugin = mainPlugin;
        _worldLength = 150;

        _gameManager = gameManager;
        _portalManager = portalManager;
        _processManager = processManager;
        _worldCopier = new WorldCopier(_mainPlugin, _gameManager.processes, _processManager);
        _customWorlds = _gameManager.customWorlds;
    }
    public Location GenerateSpawnLocation(Location referenceLocation, int spawnRadius) {
        int x;
        int z;
        //int sideLength;
        Location referenceCorner;
        //referenceCorner = world.GetReferenceCorner();
        referenceCorner = referenceLocation;
        //sideLength = _worldLength;

        Bukkit.broadcastMessage("reference corner: " + referenceCorner);
        for(int i = 0; i < 20; i++) {
            x = rand.nextInt(spawnRadius);
            z = rand.nextInt(spawnRadius);

            Location loc = new Location(Bukkit.getWorld("void_world"), referenceCorner.getX() + x, referenceCorner.getY(), referenceCorner.getZ() + z);
            loc = _gameManager.FindSurface(loc, 300, _gameManager.minWorldHeight);

            if(loc != null) {
                return loc;
            }
        }
        return referenceCorner;
    }
    //public void AddPostGenerationObjects(List<CustomVillager> customs) {
    public void AddPostGenerationObjects(StarterChestManager _chestManager) {
        SpawnStarterChests(_chestManager);
        SpawnPortals();
        //SpawnTeamVillagers(customs);
    }
    public void SpawnTeamVillagers(List<CustomVillager> customs) {
        List<CustomVillager> templateVillagers = new ArrayList<>();
        Villager currentVillager = null;


        for(CustomWorld customWorld : _customWorlds) {
            if(customs.isEmpty()) {
                for (int i = 0; i < _gameManager.normalVillagerAmount; i++) {
                    Location spawnLoc = customWorld.GetWorldSpawn();
                    CustomVillager customVillager = new CustomVillager(_mainPlugin, _gameManager.SpawnVillager(spawnLoc, _gameManager.SetRandomProfession()));
                }
                templateVillagers.addAll(customs);
            }
            else {
                for(CustomVillager customVillager : templateVillagers) {
                    Bukkit.broadcastMessage("got here");
                    Villager villager = (Villager) customVillager.getVillager().copy(customWorld.GetWorldSpawn());
                    CustomVillager customVill = new CustomVillager(_mainPlugin, villager);
                }
            }
        }
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.FARMER);
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.ARMORER);
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.BUTCHER);
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.CARTOGRAPHER);
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.FISHERMAN);
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.FLETCHER);
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.LIBRARIAN);
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.MASON);
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.SHEPHERD);
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.TOOLSMITH);
//            _gameManager.SpawnVillager(spawnLoc, Villager.Profession.WEAPONSMITH);

//            _gameManager.CreateCustomVillager("Villager0", spawnLoc, Villager.Profession.NITWIT);
//            _gameManager.CreateCustomVillager("Villager1", spawnLoc, Villager.Profession.NITWIT);

            //spawnLoc = GetRedSpawn();
    }

    public void SpawnStarterChests(StarterChestManager chestManager) {
        for(CustomWorld customWorld : _customWorlds) {
            Location worldSpawn = customWorld.GetWorldSpawn();
            Location startChestLoc = new Location(Bukkit.getWorld("void_world"), worldSpawn.getX() + 1, worldSpawn.getY(), worldSpawn.getZ() + 1);
            StarterChest starterChest = new StarterChest(startChestLoc, chestManager.GetInventory(), _gameManager.GetStarterChestList());
            starterChest.CreateChest();
            startChestLoc = null;
            worldSpawn = null;
        }
    }
    public void SpawnPortals() {
        Location portalLoc;
        CustomWorld currentOpposingWorld;

        int i = 0;
        for(CustomWorld customWorld : _customWorlds) {
            Location worldSpawn = customWorld.GetWorldSpawn();
            portalLoc = new Location(worldSpawn.getWorld(), worldSpawn.getX() + 5, 0, worldSpawn.getZ() + 5);
            portalLoc = _gameManager.FindSurface(portalLoc, 300, _gameManager.minWorldHeight);

            if (portalLoc == null) {
                portalLoc = customWorld.GetWorldSpawn().clone().add(0, 5, 0);
            }

            //Bukkit.broadcastMessage("portal loc: " + portalLoc);
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
            //Bukkit.broadcastMessage("got here");

            Portal p = new Portal(_gameManager.portals, _portalManager, currentOpposingWorld.GetWorldSpawn(), portalLoc);
            ArmorStand armorStand = (ArmorStand) portalLoc.getWorld().spawnEntity(portalLoc, EntityType.ARMOR_STAND);
            armorStand.setGlowing(true);
            armorStand.setGravity(false);
            p.Activate();
            portalLoc = null;
            worldSpawn = null;
            i++;
        }
    }
    public void BuildWorld(CustomWorld newWorld, File worldFile, ProcessManager processManager) {
        long executionTime = processManager.GetLatestExecutionTime(_gameManager.processes) + 10;
        //Bukkit.broadcastMessage("latestExecutionTime: " + executionTime);

        Queueable queueable = () -> _worldCopier.DuplicateLand(newWorld.GetReferenceCorner(), worldFile);

        Process process = new Process(executionTime, queueable);
        processManager.GetLatestExecutionTime(_gameManager.processes);

        _gameManager.processes.put(executionTime, process);

        //File file = new File( _mainPlugin.getDataFolder().getAbsolutePath() + "/output.json");
        _worldCopier.DuplicateLand(newWorld.GetReferenceCorner(), worldFile);
    }
    public int get_worldLength() {
        return _worldLength;
    }
}
