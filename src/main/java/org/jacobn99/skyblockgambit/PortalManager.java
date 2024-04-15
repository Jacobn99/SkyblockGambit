package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class PortalManager {
    Random rand = new Random();
//    JavaPlugin _mainPlugin;
//
//    public PortalManager(JavaPlugin mainPlugin) {
//        _mainPlugin = mainPlugin;
//    }

    public void TeleportIsland(Player p, Location islandSpawn) {
        Location location;
        location = FindRandomSpawn(islandSpawn);
        p.teleport(location);
    }
    public void PortalUpdate(List<Portal> portals) {
        for (Portal portal : portals) {
            if(portal.isActivated) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (IsInPortal(portal, p)) {
                        TeleportIsland(p, portal.GetOpposingIslandLocation());
                        Bukkit.broadcastMessage("guy");
                    }
                }
            }
        }
    }
    public boolean IsInPortal(Portal portal, Player p) {
        //for(Player p : Bukkit.getOnlinePlayers()) {
            Location playerLoc = p.getLocation();
//            for(Portal portal : portals) {
                Location portalLoc = portal.GetPortalLocation();
                if (playerLoc.getX() > portalLoc.getX() - 1 && playerLoc.getX() < portalLoc.getX() + 2) {
//                    Bukkit.broadcastMessage("Z Player: " + playerLoc.getZ() + ", Z Portal: " + portalLoc.getZ());
                    if(playerLoc.getZ() > portalLoc.getZ() && playerLoc.getZ() < portalLoc.getZ() + 0.8)  {
                        if(playerLoc.getY() >= portalLoc.getY() && playerLoc.getY() < portalLoc.getY() + 3) {
//                            Bukkit.broadcastMessage("guy");
                            return true;
                        }
                    }
                }
            //}
        //}
        return false;
    }

    private Location FindRandomSpawn(Location islandSpawn) {
        int x;
        int z;
        //Location loc;

        for(int i = 0; i < 15; i++) {
            //Bukkit.broadcastMessage("islandSpawn: " + islandSpawn);
            x = rand.nextInt(50) - 25;
            z = rand.nextInt(50) - 25;

            Location loc = new Location(Bukkit.getWorld("void_world"), islandSpawn.getX() + x, islandSpawn.getY(), islandSpawn.getZ() + z);
            loc = FindSurface(loc);
           //Bukkit.broadcastMessage("Loc: " + loc);

            //if (loc.getX() != islandSpawn.getX() && loc.getZ() != islandSpawn.getZ()) {
            if(loc != null) {
                //Bukkit.broadcastMessage("x: " + loc.getX() + " y: " + loc.getY());
//                Bukkit.broadcastMessage("Loc: " + loc);
//                Bukkit.broadcastMessage("islandSpawn(again): " + islandSpawn);
                return loc;
            }
//            loc = null;
//            Bukkit.broadcastMessage("loc: " + loc);
        }
        //Bukkit.broadcastMessage("Got this far!");
        return islandSpawn;
    }

    private Location FindSurface(Location loc) {
        Location scan = loc;
        for(double i = 0; i > -65; i--) {
            scan.setY(i);
            //Bukkit.broadcastMessage("Type: " + scan.getBlock().getType() + ", i: " + i);
            if(scan.getBlock().getType() != Material.AIR) {
                if(Bukkit.getWorld("void_world").getBlockAt((int)scan.getX(), (int)scan.getY() + 1, (int)scan.getZ()).getType() == Material.AIR &&
                        Bukkit.getWorld("void_world").getBlockAt((int)scan.getX(), (int)scan.getY() + 2, (int)scan.getZ()).getType() == Material.AIR) {
                    scan.setY(i + 1);
                    return scan;
                }
            }
        }
        //scan = loc;
        return null;
    }
}
