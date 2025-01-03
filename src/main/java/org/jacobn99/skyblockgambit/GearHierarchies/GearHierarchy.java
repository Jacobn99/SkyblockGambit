package org.jacobn99.skyblockgambit.GearHierarchies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GearHierarchy {
    private List<String> _hierarchy;
    public GearHierarchy() {
        _hierarchy = new ArrayList<>();
    }

    public List<String> get_hierarchy() {
        return _hierarchy;
    }

    public void set_hierarchy(List<String> _hierarchy) {
        this._hierarchy = _hierarchy;
    }
}
