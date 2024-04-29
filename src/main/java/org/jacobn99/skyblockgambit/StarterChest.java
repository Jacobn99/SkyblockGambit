package org.jacobn99.skyblockgambit;

import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;

import java.util.List;

public class StarterChest {
    List<String> _serializedInventory;
    List<String> _stringLocation;
    //BlockData _data;

    public List<String> getStringLocation() {
        return _stringLocation;
    }

    public void setStringLocation(List<String> _stringLocation) {
        this._stringLocation = _stringLocation;
    }

    public StarterChest(List<String> serializedInventory, List<String> stringLocation) {
        _serializedInventory = serializedInventory;
        _stringLocation = stringLocation;
        //_data = data;
    }
//    public BlockData getData() {
//        return _data;
//    }
//
//    public void setData(BlockData _data) {
//        this._data = _data;
//    }
    public List<String> getInventory() {
        return _serializedInventory;
    }

    public void setInventory(List<String> serializedInventory) {
        this._serializedInventory = serializedInventory;
    }
}
