package org.jacobn99.skyblockgambit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomAdvancements.AdvancementManager;
import org.jacobn99.skyblockgambit.CustomAdvancements.CustomAdvancement;
import org.jacobn99.skyblockgambit.Serialization.DeserializeMethod;
import org.jacobn99.skyblockgambit.Serialization.SerializeMethod;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    GameManager _gameManager;
    CustomAdvancement _advancement;
    //private ItemStack _item;
    private AdvancementManager _advancementManager;
    private int iteration;
    //private File _file;
    //private JavaPlugin _mainPlugin;
    //private CustomItemManager _itemManager;
    private Gson _gson;
    public DataManager() {
        //_gameManager = gameManager;
        //_advancementManager = advancementManager;
        //_itemManager = itemManager;
        //_mainPlugin = mainPlugin;
        //_file = new File(_mainPlugin.getDataFolder().getAbsolutePath() + "/CraftXConfig.json");
        iteration = 0;
        //_item = new ItemStack(Material.OAK_PLANKS);
        _gson = new Gson();


    }
//    public void UpdateDescription(File file) {
//        //Bukkit.broadcastMessage("item: " + item)
//        _item = GetItem(file);
//        Bukkit.broadcastMessage("item: " + _item.getType().name());
//        _advancementManager.ModifyAdvancement(file, "description", "Craft " + _item.getType().name());
//    }
    public List<Object> GetObjects(File file, DeserializeMethod deserializeMethod) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> serializedObjects;
        List<Object> deserializedObjects = new ArrayList<>();
        if (file.exists()) {
            try {
                Reader reader = Files.newBufferedReader(file.toPath());
                serializedObjects = _gson.fromJson(reader, listType);
                for(String s : serializedObjects) {
                    deserializedObjects.add(deserializeMethod.Deserialize(s));
                }
                //Bukkit.broadcastMessage("_item: " + _item);
                //return _item;
                return deserializedObjects;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            Bukkit.broadcastMessage("ERROR: Missing data file!");
        }
        return null;
    }
//    public void LoadFile() {
//        if(!_file.exists()) {
//            _
//        }
//    }

    public void WriteToFile(File file, List<Object> objects, SerializeMethod serializeMethod) {
//        Random rand = new Random();
//        //List<String> possibleItems = GetPossibleItems();
//        //Bukkit.broadcastMessage("possibleItems: " + possibleItems);
//
//        int index = rand.nextInt(possibleItems.size());

        List<String> serializedObjects = new ArrayList<>();
        for(Object o : objects) {
            serializedObjects.add(serializeMethod.Serialize(o));
        }
        try {
            Writer writer = Files.newBufferedWriter(file.toPath());
            _gson.toJson(serializedObjects, writer);
            //_gson.toJson(_itemManager.SerializeItem(_item), writer);
            //Bukkit.broadcastMessage("Serialized Item: " + _itemManager.SerializeItem(_item));
            //writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//    private List<String> GetPossibleItems() {
//        List<String> serializedItems = new ArrayList<>();
//        List<ItemStack> items = new ArrayList<>();
//        items.add(new ItemStack(Material.STONE));
//        items.add(new ItemStack(Material.BIRCH_PLANKS));
//        for(ItemStack item : items) {
//            serializedItems.add(_itemManager.SerializeItem(item));
//        }
//        return serializedItems;
//    }
}
