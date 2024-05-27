package org.jacobn99.skyblockgambit;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

//public class Borderwall extends GameManager {
public class Borderwall {
    Location borderLocation;
    JavaPlugin _mainPlugin;
    GameManager _gameManager;
//    public Borderwall(JavaPlugin mainPlugin) {
//        super(mainPlugin);
//        _mainPlugin = mainPlugin;
//    }
    public Borderwall(JavaPlugin mainPlugin, GameManager gameManager) {
        _gameManager = gameManager;
        _mainPlugin = mainPlugin;
    }

    public void createBorder(Location blueSpawn, Location redSpawn) {
        //Location spawn = blueSpawn;
        borderLocation = (blueSpawn.add(redSpawn)).multiply(0.5);
    }
    public Location GetBorderLocation() {
        if(borderLocation == null) {
            createBorder(_gameManager.GetBlueSpawn(), _gameManager.GetRedSpawn());
        }
        return borderLocation;
    }
//    public Location getBorderLoc() {
//        Bukkit.broadcastMessage("blueSpawn: " + blueSpawn);
//        if(borderLocation == null) {
//            Bukkit.broadcastMessage("borderLoc is null!");
//        }
//        return borderLocation;
//    }

//    public void preventPassage(Location blueSpawn, Location redSpawn) {
//        Location borderLocation;
//        borderLocation = (blueSpawn.add(redSpawn)).multiply(0.5);
//    }

}
