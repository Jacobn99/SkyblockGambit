package org.jacobn99.skyblockgambit;

import org.bukkit.inventory.ItemStack;


public class CustomItems {
    String _item;

    public CustomItems(String item) {
        _item = item;
    }

    public String getItem() {
        return _item;
    }

    public void setItem(String _item) {
        this._item = _item;
    }
}
