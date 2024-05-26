package org.jacobn99.skyblockgambit;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CustomItemManager {
    JavaPlugin _mainPlugin;
    File _itemFile;
    List<CustomItems> customItemsList;

    public CustomItemManager(JavaPlugin mainPlugin) {
        customItemsList = new ArrayList();
        _mainPlugin = mainPlugin;
        _itemFile = new File(_mainPlugin.getDataFolder(), "custom_items.json");
    }

    public ItemStack GetCustomItem(int inputIndex) {
        String serializedItem;
        ItemStack item;
        int index = 0;


        UpdateCustomItemsList();
        if(inputIndex > customItemsList.size() - 1 || customItemsList.isEmpty()) {
            Bukkit.broadcastMessage("Outside of bounds of index");
            return null;
        }
        else {
            index = inputIndex;
        }

        serializedItem = customItemsList.get(index).getItem();
        item = DeserializeItem(serializedItem);

        return item;
    }
    public List<CustomItems> GetCustomItemsList() {
        UpdateCustomItemsList();
        return customItemsList;
    }

    public int ItemNameToIndex(String name) {
        List<CustomItems> customItems;
        customItems = GetCustomItemsList();

        for(CustomItems c : customItems) {
            if(c.getItemName().equalsIgnoreCase(name)) {
                return customItems.indexOf(c);
            }
        }
        return -1;
    }
    public void LoadItemFile() {
        if(!_itemFile.exists()) {
            customItemsList.clear();

            try {
                FileWriter writer = new FileWriter(_itemFile);
                Gson gson = new Gson();

                gson.toJson(customItemsList, writer);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void UpdateCustomItemsList() {
        LoadItemFile();
        try {
            FileReader reader = new FileReader(_itemFile);
            Type type = new TypeToken<ArrayList<CustomItems>>(){}.getType();
            Gson gson = new Gson();

            customItemsList = gson.fromJson(reader, type);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void UpdateItemFile() {
        LoadItemFile();
        try {
            FileWriter writer = new FileWriter(_itemFile);
            Gson gson = new Gson();

            gson.toJson(customItemsList, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void AddCustomItem(Player p, String itemName) {
        UpdateCustomItemsList();
        customItemsList.add(new CustomItems(SerializeItem(p.getInventory().getItemInMainHand()), itemName));
        UpdateItemFile();
    }

    public void SetCustomItem(Player p, int index, String name) {
        UpdateCustomItemsList();
        if(index > customItemsList.size() - 1) {
            Bukkit.broadcastMessage("Not in bounds of index");
        }
        else {
            customItemsList.set(index, new CustomItems(SerializeItem(p.getInventory().getItemInMainHand()), name));
            UpdateItemFile();
        }
    }
    public String SerializeItem(ItemStack item) {
        String encodedItem;
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

            os.writeObject(item);
            os.flush(); //transfers data from buffer in the os ByteArrayOutputStream object to the io ByteArrayOutputStream object

            byte[] itemSerialized = io.toByteArray();

            encodedItem = Base64.getEncoder().encodeToString(itemSerialized);

            return encodedItem;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemStack DeserializeItem(String encodedItem) {
        byte[] decodedItem;
        try {
            decodedItem = Base64.getDecoder().decode(encodedItem);
            ByteArrayInputStream in = new ByteArrayInputStream(decodedItem);
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);

            ItemStack newItem = (ItemStack) is.readObject();
            return newItem;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
//
}