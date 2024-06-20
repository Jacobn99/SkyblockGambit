package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class CustomWorld {
    Location _referenceCorner;
    List<CustomWorld> _customWorlds;
    //Location _corner2;
    WorldManager _manager;
    Location _worldSpawn;


    CustomWorld(WorldManager manager, Location referenceCorner, List<CustomWorld> customWorlds) {
        _referenceCorner = referenceCorner;
        _manager = manager;
        _customWorlds = customWorlds;
        _customWorlds.add(this);
    }

    public Location GetReferenceCorner() {
        return _referenceCorner;
    }
    public Location GetWorldSpawn() {
        if(_worldSpawn == null) {
            _worldSpawn = _manager.FindRandomWorldSpawn(this);
            //Bukkit.broadcastMessage("ERROR: _worldSpawn == null");
        }
        return _worldSpawn;
    }
}
