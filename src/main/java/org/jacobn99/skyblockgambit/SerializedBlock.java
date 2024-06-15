package org.jacobn99.skyblockgambit;

import org.bukkit.Location;

public class SerializedBlock {
    private String _data;
    private double _x;
    private double _y;
    private double _z;

    public String get_data() {
        return _data;
    }

    public void set_data(String _data) {
        this._data = _data;
    }


    public double get_x() {
        return _x;
    }

    public void set_x(double _x) {
        this._x = _x;
    }

    public double get_y() {
        return _y;
    }

    public void set_y(double _y) {
        this._y = _y;
    }

    public double get_z() {
        return _z;
    }

    public void set_z(double _z) {
        this._z = _z;
    }

    public SerializedBlock(String data, double x, double y, double z) {
        _data = data;
        _x = x;
        _y = y;
        _z = z;
    }

}
