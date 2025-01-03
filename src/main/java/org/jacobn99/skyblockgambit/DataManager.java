package org.jacobn99.skyblockgambit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomAdvancements.AdvancementManager;
import org.jacobn99.skyblockgambit.CustomAdvancements.CustomAdvancement;
import org.jacobn99.skyblockgambit.Serialization.DeserializeMethod;
import org.jacobn99.skyblockgambit.Serialization.SerializeMethod;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        iteration = 0;
        _gson = new Gson();
    }
    public void LoadFile(File file) {
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<String> GetSerializedObjects(File file) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> serializedObjects;
        if (file.exists()) {
            try {
                Reader reader = Files.newBufferedReader(file.toPath());
                serializedObjects = _gson.fromJson(reader, listType);
                return serializedObjects;

            } catch (Exception e) {
                Bukkit.broadcastMessage("ERROR: File formated incorrectly or is empty so deleting file (GetSerializedObjects)");
                Bukkit.getConsoleSender().sendMessage("ERROR: File formated incorrectly or is empty so deleting file (GetSerializedObjects)");
                file.delete();
                return null;
                //throw new RuntimeException(e);
            }
        }
        else {
            Bukkit.broadcastMessage("ERROR: Missing data file!");
        }
        return null;
    }
    public List<Object> GetObjects(File file, DeserializeMethod deserializeMethod) {
        try {
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            List<String> serializedObjects;
            List<Object> deserializedObjects = new ArrayList<>();
            if (file.exists()) {
                try {
                    Reader reader = Files.newBufferedReader(file.toPath());
                    serializedObjects = _gson.fromJson(reader, listType);
                    for (String s : serializedObjects) {
                        deserializedObjects.add(deserializeMethod.Deserialize(s));
                    }
                    //Bukkit.broadcastMessage("_item: " + _item);
                    //return _item;
                    return deserializedObjects;

                } catch (IOException e) {
                    file.delete();
                    Bukkit.broadcastMessage("ERROR: File formated incorrectly or is empty so deleting file (GetObjects)");
                    throw new RuntimeException(e);
                }
            } else {
                Bukkit.broadcastMessage("ERROR: Missing data file!");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void AddObjectToFile(File file, Object o, SerializeMethod serializeMethod) {
        String addedObject;
        List<String> serializedObjects;
        //Type listType = new TypeToken<List<String>>() {}.getType();

        if (serializeMethod != null) {
            addedObject = serializeMethod.Serialize(o);
        }
        else if(o instanceof Integer) {
            addedObject = Integer.toString((Integer) o);
        }
        else {
            Bukkit.broadcastMessage("ERROR: Data Input error");
            return;
        }

        try {
            serializedObjects = GetSerializedObjects(file);

            if(serializedObjects == null) {
                serializedObjects = new ArrayList<>();
            }
            //Bukkit.broadcastMessage(addedObject);
            //Bukkit.broadcastMessage("serializedObjects: " + serializedObjects);

            serializedObjects.add(addedObject);

            Writer writer = Files.newBufferedWriter(file.toPath()); //careful, this line immedietly clears the file

            _gson.toJson(serializedObjects, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void WriteToFile(File file, List<Object> objects, SerializeMethod serializeMethod) {
        List<String> serializedObjects = new ArrayList<>();
        for(Object o : objects) {
            serializedObjects.add(serializeMethod.Serialize(o));
        }
        try {
            Writer writer = Files.newBufferedWriter(file.toPath());
            _gson.toJson(serializedObjects, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
