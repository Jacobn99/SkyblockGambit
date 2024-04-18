package org.jacobn99.skyblockgambit;

import org.bukkit.inventory.ItemStack;


public class CustomItems {
    String _serializeItem;
    String _itemName;

    public String getItemName() {
        return _itemName;
    }

    public void setItemName(String _itemName) {
        this._itemName = _itemName;
    }

    public CustomItems(String item, String itemName) {
        _serializeItem = item;
        _itemName = itemName;
    }

    public String getItem() {
        return _serializeItem;
    }

    public void setItem(String _item) {
        this._serializeItem = _item;
    }
}
