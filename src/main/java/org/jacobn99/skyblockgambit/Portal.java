package org.jacobn99.skyblockgambit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Portal {
    PortalManager _manager;
    Location _portalLoc;
    Location _opposingIslandLocation;
    String _team;
    boolean isActivated;
    public Portal(List portals, PortalManager manager, Location opposingIslandLocation, Location portalLoc) {
        _manager = manager;
        _portalLoc = portalLoc;
        _opposingIslandLocation = opposingIslandLocation;
        isActivated = false;
        portals.add(this);
    }
    public Location GetOpposingIslandLocation() {
        return _opposingIslandLocation;
    }

    public Location GetPortalLocation() {
        return _portalLoc;
    }
    public void Activate() {
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
    public void Invade(Player p, Location islandLocation) {
        _manager.TeleportIsland(p, islandLocation);
    }
}
