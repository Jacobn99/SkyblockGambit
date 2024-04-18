package org.jacobn99.skyblockgambit;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Borderwall extends GameManager {
    Location borderLocation;
    JavaPlugin _mainPlugin;
    public Borderwall(JavaPlugin mainPlugin) {
        super(mainPlugin);
        _mainPlugin = mainPlugin;
    }

    public void createBorder(Location blueSpawn, Location redSpawn) {
        //Location spawn = blueSpawn;
        borderLocation = (blueSpawn.add(redSpawn)).multiply(0.5);
    }
    public Location GetBorderLocation() {
        if(borderLocation == null) {
            createBorder(GetBlueSpawn(), GetRedSpawn());
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
