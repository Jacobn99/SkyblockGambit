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
//
////    public void GetHierarchies() {
////        YAMLFactory yamlFactory = new YAMLFactory();
////        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
////
////        try {
////            // Read the YAML file and map it to the PersonList object
////            HierarchyList hierarchyList = objectMapper.readValue(_data, HierarchyList.class);
////
////            // Output the data
////            List<GearHierarchy> gearHierarchies = hierarchyList.get_list();
////            for (GearHierarchy gearHierarchy : gearHierarchies) {
////                Bukkit.broadcastMessage("Name: " + gearHierarchy.get_name());
////                for(String gear : gearHierarchy.get_hierarchy()) {
////                    Bukkit.broadcastMessage("- " + gear);
////                }
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//    public void GetHierarchies() {
//        File file = new File("src/main/resources/gear_hierarchies.yml");
//        if(file.exists()) {
//            file.delete();
//        }
//        try {
//            file.createNewFile();
//            Bukkit.broadcastMessage("exists?: " + file.exists());
//
//            BufferedReader reader = new BufferedReader(new FileReader(_data.getAbsolutePath()));
//            BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    writer.write(line);
//                    writer.newLine();
//                }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        File file = new File("sigma.txt");
////        try {
////            file.createNewFile();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
////        Yaml yaml = new Yaml();
////        Bukkit.broadcastMessage("exists?: " + _data.exists());
////        InputStream inputStream = this.getClass()
////                .getClassLoader()
////                .getResourceAsStream(_data.getPath());
////
////        if (inputStream == null) {
////            Bukkit.getLogger().warning("Could not find resource: " + _data.getPath());
////            return;
////        }
////        Map<String, Object> obj = yaml.load(inputStream);
////        System.out.println(obj);
////        Bukkit.broadcastMessage("obj: " + obj);
//    //
//    //    try {
//    //        // Read the YAML file and map it to the PersonList object
//    //        GearHierarchy gearHierarchy = objectMapper.readValue(_data, GearHierarchy.class);
//    //        for(String gear : gearHierarchy.get_hierarchy()) {
//    //            Bukkit.broadcastMessage("- " + gear);
//    //        }
//    //    } catch (IOException e) {
//    //        e.printStackTrace();
//    //    }
//    }
}
