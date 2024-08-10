package org.jacobn99.skyblockgambit;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.jacobn99.skyblockgambit.CustomWorlds.WorldManager;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;

import java.util.*;

public class NetherManager {
    GameManager _gameManager;
    Location _fortressLoc;
    World nether;
    ProcessManager _processManager;
    WorldManager _worldManager;
    private AreaDetection _areaDetection;
    public HashMap<Player, Location> netherPortalLocations;
    public int protectionLength;
    public int protectHeight;


    public NetherManager(GameManager _gameManager, ProcessManager processManager, WorldManager worldManager) {
        this._gameManager = _gameManager;
        _processManager = processManager;
        _fortressLoc = null;
        nether = Bukkit.getWorld("void_world_nether");
        _worldManager = worldManager;
        netherPortalLocations = new HashMap<>();
        _areaDetection = new AreaDetection();
        protectionLength = 10;
        protectHeight = 20;
//        playersInNether = new ArrayList<>();

    }
    public boolean ProtectNetherSpawns(Block block) {
        for(Team team : _gameManager.teams) {
            Location netherSpawn = team.GetNetherSpawn();
            if(netherSpawn != null) {
                Location corner1 = netherSpawn.clone().add((-protectionLength)/2, 0, (-protectionLength)/2);
                Location corner2 = netherSpawn.clone().add((protectionLength)/2, protectHeight, (protectionLength)/2);

//                corner1.getBlock().setType(Material.BLUE_CONCRETE);
//                corner2.getBlock().setType(Material.YELLOW_CONCRETE);
                if (_areaDetection.IsBetweenCorners(block.getLocation(), corner1, corner2)) {
                    return true;
                    //event.setCancelled(false);
                }
            }
        }
        return false;
    }
    public void ClearSpawnPortalArea(Location portalLoc) {
        Location referenceCorner = portalLoc.clone();
        int removeLength = ((protectionLength)/2) + 1;
        referenceCorner.add(-removeLength, 0, -removeLength);
        for(int x = 0; x < removeLength * 2 + 1; x++) {
            for(int z = 0; z < removeLength * 2 + 1; z++) {
                for(int y = 0; y < 5; y++) {
                    Location currentLoc = new Location(portalLoc.getWorld(), x + referenceCorner.getX(), y + referenceCorner.getY(), z + referenceCorner.getZ());
                    currentLoc.getBlock().setType(Material.AIR);
                    currentLoc = null;
                }
            }
        }
    }
    private Location GenerateRandomNetherSpawn() {
        Location spawn = null;
        int i = 0;
        spawn = _worldManager.GenerateSpawnLocation(nether, GetFortressLoc(), 100, 30, 200);
        return spawn;
    }

    private Location GetFortressLoc() {
        if(_fortressLoc == null) {
            _fortressLoc = nether.locateNearestStructure(new Location(nether, 0, 0 ,0), StructureType.NETHER_FORTRESS, 5000, false);
            _fortressLoc.setY(0);
        }
        return _fortressLoc;
    }
    public void CorrectNetherSpawning(PlayerPortalEvent event) {
        try {
            //Bukkit.broadcastMessage("bro");
            Player p = (Player) event.getPlayer();
            _processManager.CreateProcess(_gameManager.processes,
                    Bukkit.getWorld("void_world").getFullTime() + 2, () -> DimensionCheck(p));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void DimensionCheck(Player p) {
        try {
            Team team = _gameManager.FindPlayerTeam(p);

            if(team!= null) {
                if (p.getLocation().getWorld() == nether) {
                    Location loc = GenerateRandomNetherSpawn();
                    Bukkit.broadcastMessage("fortressLoc: " + loc);
                    TeleportToNetherSpawn(p, team);
                }
                else if (p.getLocation().getWorld() == Bukkit.getWorld("void_world")) {
                    Location loc = GenerateRandomNetherSpawn();
                    p.teleport(team.GetTeamWorld().GetWorldSpawn(_gameManager));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void TeleportToNetherSpawn(Player p, Team team) {
        if(team.GetNetherSpawn() == null) {
            team.SetNetherSpawn(GenerateRandomNetherSpawn());
            GenerateNetherPortal(team.GetNetherSpawn());
        }
        p.teleport(team.GetNetherSpawn());

    }
    public void GenerateNetherPortal(Location location) {
        Location loc = location.clone();
        ClearSpawnPortalArea(location);

        GenerateNetherPortalFrame(location);


        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 2; x++) {
                Location currentLoc = loc.clone();
                currentLoc.setY(loc.getY() + y);
                currentLoc.setX(loc.getX() + x);
                currentLoc.getBlock().setType(Material.NETHER_PORTAL);
                currentLoc = null;
            }
        }
    }
    public void GenerateNetherPortalFrame(Location location) {
        Location loc = location.clone();
        loc.add(-1, 0, 0);
        for(int e = 0; e < 2; e++) {
            for (int i = 0; i < 3; i++) {
                Location currentLoc = loc.clone();
                currentLoc.setY(loc.getY() + i);
                currentLoc.setX(loc.getX() + (e*3));
                currentLoc.getBlock().setType(Material.OBSIDIAN);
                currentLoc = null;
            }
        }
        loc.add(0, -1, 0);

        for(int e = 0; e < 2; e++) {
            for (int i = 0; i < 4; i++) {
                Location currentLoc = loc.clone();
                currentLoc.setX(loc.getX() + i);
                currentLoc.setY(loc.getY() + (e*4));
                currentLoc.getBlock().setType(Material.OBSIDIAN);
                currentLoc = null;

            }
        }
    }
}
