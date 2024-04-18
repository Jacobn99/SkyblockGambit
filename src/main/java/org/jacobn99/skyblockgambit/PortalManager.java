package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class PortalManager {
    Random rand = new Random();
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
            Location playerLoc = p.getLocation();
                Location portalLoc = portal.GetPortalLocation();
                if (playerLoc.getX() > portalLoc.getX() - 1 && playerLoc.getX() < portalLoc.getX() + 2) {
                    if(playerLoc.getZ() > portalLoc.getZ() && playerLoc.getZ() < portalLoc.getZ() + 0.8)  {
                        if(playerLoc.getY() >= portalLoc.getY() && playerLoc.getY() < portalLoc.getY() + 3) {
                            return true;
                        }
                    }
                }

        return false;
    }

    private Location FindRandomSpawn(Location islandSpawn) {
        int x;
        int z;

        for(int i = 0; i < 15; i++) {
            x = rand.nextInt(50) - 25;
            z = rand.nextInt(50) - 25;
            Location loc = new Location(Bukkit.getWorld("void_world"), islandSpawn.getX() + x, islandSpawn.getY(), islandSpawn.getZ() + z);
            loc = FindSurface(loc);

            if(loc != null) {
                return loc;
            }
        }
        return islandSpawn;
    }

    private Location FindSurface(Location loc) {
        Location scan = loc;
        for(double i = 0; i > -65; i--) {
            scan.setY(i);
            if(scan.getBlock().getType() != Material.AIR) {
                if(Bukkit.getWorld("void_world").getBlockAt((int)scan.getX(), (int)scan.getY() + 1, (int)scan.getZ()).getType() == Material.AIR &&
                        Bukkit.getWorld("void_world").getBlockAt((int)scan.getX(), (int)scan.getY() + 2, (int)scan.getZ()).getType() == Material.AIR) {
                    scan.setY(i + 1);
                    return scan;
                }
            }
        }
        return null;
    }
}
