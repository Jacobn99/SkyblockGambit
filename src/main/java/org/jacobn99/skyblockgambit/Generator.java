package org.jacobn99.skyblockgambit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public interface Generator {
    ItemStack GetItem();
    //void SetLocation(Location loc);
    long GetGenerateDelay();
    void AddGenerateTime(long time);
    void AddFuelTime(long time);
    long GetGenerateTimeRemaining();
    long GetFuelDelay();
    long GetFuelTimeRemaining();
    Location GetLocation();
    //void SetFuelChest(Location blockLoc);
    boolean IsFuelAvailable();
    void Generate();
    Chest GetFuelChest();
    ItemStack GetFuel();
}


