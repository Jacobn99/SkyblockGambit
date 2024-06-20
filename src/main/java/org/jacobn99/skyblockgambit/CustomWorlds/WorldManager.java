package org.jacobn99.skyblockgambit.CustomWorlds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Portals.Portal;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;

import java.io.File;
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
    public WorldManager(JavaPlugin mainPlugin, GameManager gameManager, PortalManager portalManager) {
        rand = new Random();
        _mainPlugin = mainPlugin;
        _worldLength = 300;

        _gameManager = gameManager;
        _portalManager = portalManager;
        _worldCopier = new WorldCopier(_mainPlugin, _gameManager.processes);
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
//    public void SpawnTeamVillagers() {
//        for(CustomWorld customWorld : _customWorlds) {
//            Location spawnLoc = customWorld.GetWorldSpawn();
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
    public void BuildWorld(CustomWorld newWorld, File worldFile) {
        //ProcessManager _processManager = new ProcessManager();
        //File file = new File( _mainPlugin.getDataFolder().getAbsolutePath() + "/output.json");
        _worldCopier.DuplicateLand(newWorld.GetReferenceCorner(), worldFile);
    }
    public int get_worldLength() {
        return _worldLength;
    }
}
