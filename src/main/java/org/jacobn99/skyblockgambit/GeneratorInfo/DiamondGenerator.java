package org.jacobn99.skyblockgambit.GeneratorInfo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.jacobn99.skyblockgambit.Generator;

import java.util.List;

public class DiamondGenerator implements Generator {
    Location _loc;
    int _fuelAmount = 1;
    ItemStack _item = new ItemStack(Material.DIAMOND);
    ItemStack _fuel = new ItemStack(Material.STONE, _fuelAmount);
    Chest _fuelChest;
    long _generateDelay;
    long _generateTimeRemaining;
    long _fuelDelay;
    long _fuelTimeRemaining;

    public DiamondGenerator(List genList, Location genLoc, Location chestLoc) {
        Bukkit.getWorld("void_world").getBlockAt(chestLoc).setType(Material.CHEST);

        _generateDelay = 100;
        _generateTimeRemaining = _generateDelay;

        _fuelDelay = 10;
        _fuelTimeRemaining = _fuelDelay;

        _loc = genLoc;
        _fuelChest = (Chest) Bukkit.getWorld("void_world").getBlockAt(chestLoc).getState();
        genList.add(this);
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
    public long GetFuelDelay() {
        return _fuelDelay;
    }
    public long GetFuelTimeRemaining() {
        return _fuelTimeRemaining;
    }
    public void AddGenerateTime(long time) {
        _generateTimeRemaining += time;
    }
    public void AddFuelTime(long time) {
        _fuelTimeRemaining += time;
    }
    public boolean IsFuelAvailable() {
        for(ItemStack i : _fuelChest.getBlockInventory().getContents()) {
            if(i != null && i.getAmount() >= _fuelAmount) {
                if (i.getType() == _fuel.getType()) {
                    return true;
                }
            }
        }
        return false;
    }
    public Chest GetFuelChest() {
        return _fuelChest;
    }

    public Location GetLocation() {
        return _loc;
    }
    public void Generate() {
        Bukkit.getWorld("void_world").dropItemNaturally(_loc, _item);
    }
    public ItemStack GetFuel() {
        return _fuel;
    }
}
