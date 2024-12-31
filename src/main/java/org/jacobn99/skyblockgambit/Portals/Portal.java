package org.jacobn99.skyblockgambit.Portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class Portal {
    PortalManager _manager;
    Location _portalLoc;
    Location _opposingIslandLocation;
    String _team;
    boolean isActivated;
    public Portal(List<Portal> portals, PortalManager manager, Location opposingIslandLocation, Location portalLoc) {
        _manager = manager;
        _portalLoc = portalLoc;
        _opposingIslandLocation = opposingIslandLocation;
        isActivated = false;
        portals.add(this);
        Bukkit.broadcastMessage("Portal Created");
    }
    public Location GetOpposingIslandLocation() {
        return _opposingIslandLocation;
    }

    public Location GetPortalLocation() {
        return _portalLoc;
    }
    public void Activate() {
//        Bukkit.broadcastMessage("Activated");

        Location loc = new Location(_portalLoc.getWorld(), _portalLoc.getX() - 1, _portalLoc.getY(), _portalLoc.getZ());
        isActivated = true;

        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 3; c++) {
                loc.getBlock().setType(Material.NETHER_PORTAL);
                loc.setX(loc.getX() + 1);
            }
            loc.setX(loc.getX() - 3);
            loc.setY(loc.getY() + 1);
        }
    }

    public void Deactivate() {
//        Bukkit.broadcastMessage("Deactivated");
        Location loc = new Location(_portalLoc.getWorld(), _portalLoc.getX() - 1, _portalLoc.getY(), _portalLoc.getZ());
        isActivated = false;

        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 3; c++) {
                loc.getBlock().breakNaturally();
                loc.setX(loc.getX() + 1);
            }
            loc.setX(loc.getX() - 3);
            loc.setY(loc.getY() + 1);
        }
    }
    public void RemovePortal() {
        Bukkit.broadcastMessage("Portal removed");
        Location loc = new Location(_portalLoc.getWorld(), _portalLoc.getX() - 2, _portalLoc.getY() - 1, _portalLoc.getZ());
        isActivated = false;
        for(int r = 0; r < 6; r++) {
            for(int c = 0; c < 6; c++) {
                loc.getBlock().breakNaturally();
                loc.setX(loc.getX() + 1);
            }
            loc.setX(loc.getX() - 6);
            loc.setY(loc.getY() + 1);
        }
    }
    public void Invade(Player p, Location islandLocation) {
        _manager.TeleportIsland(p, islandLocation);
    }
}
