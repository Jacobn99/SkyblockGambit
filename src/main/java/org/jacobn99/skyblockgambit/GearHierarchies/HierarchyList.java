package org.jacobn99.skyblockgambit.GearHierarchies;

import java.util.ArrayList;
import java.util.List;

public class HierarchyList {
    List<GearHierarchy> _list;

    public HierarchyList() {
        _list = new ArrayList<>();
    }

    public List<GearHierarchy> get_list() {
        return _list;
    }

    public void set_list(List<GearHierarchy> _list) {
        this._list = _list;
    }
}
