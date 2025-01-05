package org.jacobn99.skyblockgambit.CustomWorlds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Portals.Portal;

import java.util.List;

public class CustomWorld {
    private final Location _middleLoc;
    List<CustomWorld> _customWorlds;
    //Location _corner2;
    WorldManager _manager;
    Location _worldSpawn;
    Portal _worldPortal;


    public CustomWorld(WorldManager manager, Location middleLoc, List<CustomWorld> customWorlds) {
        _middleLoc = middleLoc;
        _manager = manager;
        _customWorlds = customWorlds;
        _customWorlds.add(this);
        _worldPortal = null;
//        Bukkit.broadcastMessage("middle loc back back: " + _middleLoc.toVector().toString());

    }

    public Location GetMiddleLoc() {
        return _middleLoc.clone();
    }
    public Location GetWorldSpawn(GameManager _gameManager) {
        if(_worldSpawn == null) {
//            Bukkit.broadcastMessage("middle back: " + this._middleLoc.toVector().toString());
//            _worldSpawn = _manager.GenerateSpawnLocation(Bukkit.getWorld("void_world"), this._referenceCorner, 300, _gameManager.minWorldHeight, _manager.get_worldLength());
            _worldSpawn = _manager.GenerateSpawnLocation(Bukkit.getWorld("void_world"), this._middleLoc, _gameManager.minWorldHeight + 150, _gameManager.minWorldHeight, _manager.get_worldLength()/2);

            //Bukkit.broadcastMessage("ERROR: _worldSpawn == null");
        }
        return _worldSpawn.clone();
    }
    public Portal GetWorldPortal() {
        return _worldPortal;
    }

    public void SetWorldPortal(Portal worldPortal) {
        this._worldPortal = worldPortal;
    }
}
