package org.jacobn99.skyblockgambit.CustomWorlds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomVillager;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Portals.Portal;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.Processes.Process;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;

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
        _worldLength = 300;

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
    public void AddPostGenerationObjects(List<CustomVillager> customs) {
        SpawnPortals();
        //SpawnTeamVillagers(customs);
    }
    public void SpawnTeamVillagers(List<CustomVillager> customs) {
//        for(int i = 0; i < _gameManager.normalVillagerAmount; i++) {
//            professions.add(_gameManager.SetRandomProfession());
//        }
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

    public void SpawnPortals() {
        Location portalLoc;
        //CustomWorld currentWorld;
        CustomWorld currentOpposingWorld;

        int i = 0;
        for(CustomWorld customWorld : _customWorlds) {
            portalLoc = _gameManager.FindSurface(customWorld.GetWorldSpawn(), 300, _gameManager.minWorldHeight);

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
            p.Activate();
            i++;
        }
    }
    public void BuildWorld(CustomWorld newWorld, File worldFile, ProcessManager processManager) {
        long executionTime = processManager.GetLatestExecutionTime(_gameManager.processes) + 10;
        Bukkit.broadcastMessage("latestExecutionTime: " + executionTime);

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
