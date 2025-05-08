package org.jacobn99.skyblockgambit.GearHierarchies;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.DataManager;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;
import java.util.Map;

public class GearHierarchyManager {
    JavaPlugin _mainPlugin;
    CustomItemManager _itemManager;
    private File _data;
    private DataManager _dataManager;
//    private ObjectMapper _objectMapper;
    public GearHierarchyManager(JavaPlugin mainPlugin, DataManager dataManager, CustomItemManager itemManager) {
        _mainPlugin = mainPlugin;
        _itemManager = itemManager;
        _dataManager = dataManager;

        _data = new File(_mainPlugin.getDataFolder() + "\\gear_hierarchies.yml");
//        _dataManager.LoadFile(_data);
//        _objectMapper = new ObjectMapper(new YAMLFactory());
    }
}
