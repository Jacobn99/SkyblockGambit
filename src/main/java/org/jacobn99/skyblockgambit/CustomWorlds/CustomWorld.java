package org.jacobn99.skyblockgambit.CustomWorlds;

import org.bukkit.Location;
import org.jacobn99.skyblockgambit.GameManager;

import java.util.List;

public class CustomWorld {
    Location _referenceCorner;
    List<CustomWorld> _customWorlds;
    //Location _corner2;
    WorldManager _manager;
    Location _worldSpawn;


    public CustomWorld(WorldManager manager, Location referenceCorner, List<CustomWorld> customWorlds) {
        _referenceCorner = referenceCorner;
        _manager = manager;
        _customWorlds = customWorlds;
        _customWorlds.add(this);
    }

    public Location GetReferenceCorner() {
        return _referenceCorner;
    }
    public Location GetWorldSpawn(GameManager _gameManager) {
        if(_worldSpawn == null) {
            _worldSpawn = _manager.GenerateSpawnLocation(this._referenceCorner, _manager.get_worldLength());
            //Bukkit.broadcastMessage("ERROR: _worldSpawn == null");
        }
        return _worldSpawn.clone();
    }
}
