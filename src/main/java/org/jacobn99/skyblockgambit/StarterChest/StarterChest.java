package org.jacobn99.skyblockgambit.StarterChest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StarterChest {
    //List<String> _serializedInventory;
    Location _chestLoc;
    ItemStack[] _inventory;
    //BlockData _data;

    public StarterChest(Location chestLoc, ItemStack[] inventory, List<StarterChest> starterChestList) {
        //_serializedInventory = serializedInventory;
        _chestLoc = chestLoc;
        _inventory = inventory;
        starterChestList.add(this);
    }

    public void DestroyChest() {
        _chestLoc.getBlock().setType(Material.AIR);
    }
    public void CreateChest() {
        _chestLoc.getBlock().setType(Material.CHEST);
        Chest chest = (Chest) _chestLoc.getBlock().getState();

        chest.getBlockInventory().setContents(_inventory);

    }
//    public void SetInventory(ItemStack[] inventory) {
//        Chest chest = (Chest) _chestLoc.getBlock().getState();
//        chest.getBlockInventory().setContents(inventory);
//    }

    public Location GetLocation() {
        return _chestLoc;
    }
//    public BlockData getData() {
//        return _data;
//    }
//
//    public void setData(BlockData _data) {
//        this._data = _data;
//    }
//    public List<String> getInventory() {
//        return _serializedInventory;
//    }

//    public void setInventory(List<String> serializedInventory) {
//        this._serializedInventory = serializedInventory;
//    }
}
