package org.jacobn99.skyblockgambit;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class ConfigManager {
    JavaPlugin _mainPlugin;
    File _itemFile;
    HashMap<String, ItemStack> _specialItems = new HashMap<>();
    List<CustomItems> customItemsList;
    public ConfigManager(JavaPlugin mainPlugin) {
        customItemsList = new ArrayList();
        _mainPlugin = mainPlugin;
        _itemFile = new File(_mainPlugin.getDataFolder(), "custom_items.json");
    }

//    public void DefineSpecialItems() {
//        //_specialItems.put("REDSTONE_KIT", )
//    }

    public ItemStack GetCustomItem(int inputIndex) {
        String serializedItem;
        ItemStack item;
        int index = 0;

        if(inputIndex > customItemsList.size() - 1) {
            index = customItemsList.size() - 1;
            Bukkit.broadcastMessage("Index too big. Getting the index instead: " + (customItemsList.size() - 1));
        }
        else {
            index = inputIndex;
        }

        UpdateCustomItemsList();
        serializedItem = customItemsList.get(index).getItem();
        item = DeserializeItem(serializedItem);

        return item;
//        try {
//            FileReader reader = new FileReader(_itemFile);
//            Type type = new TypeToken<ArrayList<CustomItems>>(){}.getType();
//            Gson gson = new Gson();
//
//            customItemsList = gson.fromJson(reader, type);
//            reader.close();
//
//            serializedItem = customItemsList.get(itemIndex).getItem();
//
//            item = DeserializeItem(serializedItem);
//
//            return item;
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
    private String SerializeItem(ItemStack item) {
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

    private ItemStack DeserializeItem(String encodedItem) {
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
    public void LoadItemFile() {
        if(!_itemFile.exists()) {
            //customItemsList = new ArrayList();
            customItemsList.add(new CustomItems(SerializeItem(Bukkit.getPlayerExact("jacobn99").getInventory().getItemInMainHand())));

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

    public void AddCustomItem(Player p) {
        UpdateCustomItemsList();
        customItemsList.add(new CustomItems(SerializeItem(p.getInventory().getItemInMainHand())));
        UpdateItemFile();
    }
    public void SetCustomItem(Player p, int index) {
        //Bukkit.broadcastMessage("size: " + customItemsList.size());
        UpdateCustomItemsList();
        if(index > customItemsList.size() - 1) {
            customItemsList.add(new CustomItems(SerializeItem(Bukkit.getPlayerExact("jacobn99").getInventory().getItemInMainHand())));
            Bukkit.broadcastMessage("Size: " + customItemsList.size());
        }
        else {
            customItemsList.set(index, new CustomItems(SerializeItem(Bukkit.getPlayerExact("jacobn99").getInventory().getItemInMainHand())));
        }
        UpdateItemFile();
    }
//    private void AddToFile() {
//        try {
//            FileWriter writer = new FileWriter(_itemFile);
//            Gson gson = new Gson();
//
//            gson.toJson(customItemsList, writer);
//            writer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public List<String> GetArguments(String section, String subsection, String element) {
        int i = 0;
        List<Character> currentWord = new ArrayList();
        List<String> arguments = new ArrayList();
        String trades = _mainPlugin.getConfig().getConfigurationSection(section)
                .getConfigurationSection(subsection).getString(element);

        if(trades == null) {
            return null;
        }
        char[] tradesChar = trades.toCharArray();


//        Bukkit.broadcastMessage(villager1);
//        Bukkit.broadcastMessage("length: " + villager1Char.length);
        for(char c : tradesChar) {
            if(c != ' ' && c != ',') {
                currentWord.add(c);
            }
            if(c == ',' || i == tradesChar.length - 1) {
                int ii = 0;
                char[] ch = new char[currentWord.size()];

                for(char c2 : currentWord) {
                    ch[ii] = c2;
                    ii++;
                }
                arguments.add(String.valueOf(ch));
                currentWord.clear();
            }
            i++;
        }
        return arguments;
    }
}
