package org.jacobn99.skyblockgambit.GeneratorInfo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemGenerator {
    Location _loc;
    //int _fuelAmount = 1;
    ItemStack _item;
    //ItemStack _fuel = new ItemStack(Material.STONE, _fuelAmount);
    //Chest _fuelChest;
    long _generateDelay;
    long _generateTimeRemaining;
    //long _fuelDelay;
    //long _fuelTimeRemaining;
    boolean isActive;
    ItemStack _cost;

    public ItemGenerator(ItemStack item, long generateDelay, List genList) {
        _generateDelay = generateDelay;
        _generateTimeRemaining = _generateDelay;
        _item = item;

        //_fuelDelay = 10;
        //_fuelTimeRemaining = _fuelDelay;
        isActive = false;
        //_loc = genLoc;
//        _fuelChest = (Chest) Bukkit.getWorld("void_world").getBlockAt(chestLoc).getState();
        genList.add(this);
    }
    public void CreateGenerator(Location genLoc) {
        //Bukkit.getWorld("void_world").getBlockAt(chestLoc).setType(Material.CHEST);
        //_fuelChest = (Chest) Bukkit.getWorld("void_world").getBlockAt(chestLoc).getState();
        _loc = genLoc;
        Location platformLoc = _loc.clone();
        platformLoc.add(0, -1, 0);
        platformLoc.getBlock().setType(Material.QUARTZ_BLOCK);
        isActive = true;
    }

        public ItemStack GetItem() {
        return _item;
    }
    public long GetGenerateDelay() {
        return _generateDelay;
    }
    public long GetGenerateTimeRemaining() {
        return _generateTimeRemaining;
    }
//    public long GetFuelDelay() {
//        return _fuelDelay;
//    }
//    public long GetFuelTimeRemaining() {
//        return _fuelTimeRemaining;
//    }
    public void AddGenerateTime(long time) {
        _generateTimeRemaining += time;
    }
//    public void AddFuelTime(long time) {
//        _fuelTimeRemaining += time;
//    }
//    public boolean IsFuelAvailable() {
//        for(ItemStack i : _fuelChest.getBlockInventory().getContents()) {
//            if(i != null && i.getAmount() >= _fuelAmount) {
//                if (i.getType() == _fuel.getType()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//    public Chest GetFuelChest() {
//        return _fuelChest;
//    }

    public Location GetLocation() {
        return _loc;
    }
    public void Generate() {
        if(isActive) {
            Bukkit.getWorld("void_world").dropItemNaturally(_loc, _item);
        }
    }

    public ItemStack GetCost() {
        return _cost;
    }

    public void SetCost(ItemStack _cost) {
        this._cost = _cost;
    }
//    public ItemStack GetFuel() {
//        return _fuel;
//    }
}
