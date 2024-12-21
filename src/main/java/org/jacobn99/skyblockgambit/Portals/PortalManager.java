package org.jacobn99.skyblockgambit.Portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.AreaDetection;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;

import java.util.*;

public class PortalManager {
    Random rand = new Random();
    private GameManager _gameManager;
    private ProcessManager _processManager;
    public Map<Player, Long> invaders;
    private AreaDetection _areaDetection;
    private JavaPlugin _mainPlugin;
    World world;

    public PortalManager(GameManager gameManager, ProcessManager processManager, JavaPlugin mainPlugin) {
        _mainPlugin = mainPlugin;
        _gameManager = gameManager;
        _processManager = processManager;
        world = Bukkit.getWorld("void_world");
        invaders = new HashMap<>();
        _areaDetection = new AreaDetection();
    }
    public void TeleportIsland(Player p, Location islandSpawn) {
        Location location;
        location = FindRandomSpawn(islandSpawn, _gameManager);
        p.teleport(location);
    }
    public void PortalUpdate(List<Portal> portals, long tickRate) {
        for (Portal portal : portals) {
            //Bukkit.broadcastMessage("portal Location: " + portal.GetPortalLocation());
            if(portal.isActivated) {
                if(CheckPortalBreak(portal)) {
                    portal.Activate();
                }
                //Bukkit.broadcastMessage("Got here");
                for (Player p : _gameManager.participatingPlayers) {
                    if (IsInPortal(portal, p)) {
                        TeleportIsland(p, portal.GetOpposingIslandLocation());
                        portal.Deactivate();
//                        Bukkit.broadcastMessage("guy");
                        invaders.put(p, (long) 2400);
                        //_processManager.CreateProcess(_gameManager.processes, world.getFullTime() + 2400, ()-> p.teleport(_gameManager.FindPlayerTeam(p).GetTeamWorld().GetWorldSpawn(_gameManager)));
                    }
                }
            }
        }
        UpdateInvaderTimer(tickRate);
    }
    public void UpdateInvaderTimer(long tickRate) {
        long timeRemaining;
        List<Player> finishedPlayers = new ArrayList<>();
        for(Player p : invaders.keySet()) {
            timeRemaining = invaders.get(p);
            timeRemaining -= tickRate;
            invaders.replace(p, timeRemaining);

            if(timeRemaining % 5 == 0) {
                String text = "Time until teleported back: " + (timeRemaining / 20);
                String command = "title " + p.getName() + " actionbar {\"text\":\"" + text +"\"}";
                _mainPlugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }

            if(timeRemaining < 0) {
                finishedPlayers.add(p);
                p.teleport(_gameManager.FindPlayerTeam(p).GetTeamWorld().GetWorldSpawn(_gameManager));
            }
        }
        for(Player p : finishedPlayers) {
            invaders.remove(p);
        }
        finishedPlayers = null;

    }
    public boolean CheckPortalBreak(Portal portal) {
        if(portal.GetPortalLocation().getBlock().getType() != Material.NETHER_PORTAL) {
            return true;
        }
        return false;
    }
    public boolean IsInPortal(Portal portal, Player p) {
        Location playerLoc = p.getLocation();
        Location portalLoc = portal.GetPortalLocation();

        return _areaDetection.IsInArea(p.getLocation(), portalLoc.getX() + 2, portalLoc.getX() - 1,
                portalLoc.getY(), portalLoc.getY() + 3, portalLoc.getZ(), portalLoc.getZ() + 0.8);
    }

    private Location FindRandomSpawn(Location islandSpawn, GameManager gameManager) {
        int x;
        int z;

        for(int i = 0; i < 15; i++) {
            x = rand.nextInt(100) - 50;
            z = rand.nextInt(50) - 50;
            Location loc = new Location(Bukkit.getWorld("void_world"), islandSpawn.getX() + x, islandSpawn.getY(), islandSpawn.getZ() + z);
            loc = gameManager.FindSurface(loc, 300, _gameManager.minWorldHeight);

            if(loc != null) {
                return loc;
            }
        }
        return islandSpawn;
    }
}
