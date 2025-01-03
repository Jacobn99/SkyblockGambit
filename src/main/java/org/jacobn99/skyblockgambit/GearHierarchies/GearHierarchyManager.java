package org.jacobn99.skyblockgambit.GearHierarchies;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.DataManager;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

//    public void GetHierarchies() {
//        YAMLFactory yamlFactory = new YAMLFactory();
//        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
//
//        try {
//            // Read the YAML file and map it to the PersonList object
//            HierarchyList hierarchyList = objectMapper.readValue(_data, HierarchyList.class);
//
//            // Output the data
//            List<GearHierarchy> gearHierarchies = hierarchyList.get_list();
//            for (GearHierarchy gearHierarchy : gearHierarchies) {
//                Bukkit.broadcastMessage("Name: " + gearHierarchy.get_name());
//                for(String gear : gearHierarchy.get_hierarchy()) {
//                    Bukkit.broadcastMessage("- " + gear);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void GetHierarchies() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        Bukkit.broadcastMessage("exists?: " + _data.exists());
    //
    //    try {
    //        // Read the YAML file and map it to the PersonList object
    //        GearHierarchy gearHierarchy = objectMapper.readValue(_data, GearHierarchy.class);
    //        for(String gear : gearHierarchy.get_hierarchy()) {
    //            Bukkit.broadcastMessage("- " + gear);
    //        }
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    }
}
