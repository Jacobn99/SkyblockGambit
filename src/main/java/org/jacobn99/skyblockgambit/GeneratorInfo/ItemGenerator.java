package org.jacobn99.skyblockgambit.GeneratorInfo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
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
    private ArmorStand _clockDisplay;
    ItemStack _cost;

    public ItemGenerator(ItemStack item, long generateDelay, List genList) {
        _generateDelay = generateDelay;
        _generateTimeRemaining = _generateDelay;
        _item = item;
        isActive = false;
        genList.add(this);
    }
    public void CreateGenerator(Location genLoc) {
        _loc = genLoc;
        Location platformLoc = _loc.clone();
        platformLoc.add(0, -1, 0);
        platformLoc.getBlock().setType(Material.QUARTZ_BLOCK);
        ArmorStand armorStand = (ArmorStand) genLoc.getWorld().spawnEntity(genLoc, EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setInvisible(true);
        armorStand.addScoreboardTag("disposable");
        _clockDisplay = armorStand;
        isActive = true;
    }

    public ArmorStand GetClockDisplay() {
        return _clockDisplay;
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
    public void AddGenerateTime(long time) {
        _generateTimeRemaining += time;
    }

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
