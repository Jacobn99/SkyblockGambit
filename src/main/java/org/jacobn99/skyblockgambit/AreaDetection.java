package org.jacobn99.skyblockgambit;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AreaDetection {
    public boolean IsBetweenCorners(Location loc, Location corner1, Location corner2) {
        return IsInArea(loc, corner1.getX(), corner2.getX(), corner1.getY(), corner2.getY(), corner1.getZ(), corner2.getZ());
    }
   public boolean IsInArea(Location loc, double x0, double x1, double y0, double y1, double z0, double z1) {
        double largeX = LargestNumber(x0, x1);
        double smallX = SmallestNumber(x0, x1);
        double largeY = LargestNumber(y0, y1);
        double smallY = SmallestNumber(y0, y1);
        double largeZ = LargestNumber(z0, z1);
        double smallZ = SmallestNumber(z0, z1);
        if (loc.getX() > smallX && loc.getX() < largeX) {
//            Bukkit.broadcastMessage("x good");
            if (loc.getZ() > smallZ && loc.getZ() < largeZ) {
//                Bukkit.broadcastMessage("z good");
                if (loc.getY() >= smallY && loc.getY() < largeY + 3) {
//                    Bukkit.broadcastMessage("y good");
                    return true;
                }
            }
        }

        return false;
    }

    private double LargestNumber(double i0, double i1) {
        if(i0 >= i1) {
            return i0;
        }
        else {
            return i1;
        }
    }
    private double SmallestNumber(double i0, double i1) {
        if(i0 <= i1) {
            return i0;
        }
        else {
            return i1;
        }
    }
}
