package org.jacobn99.skyblockgambit.CustomItems;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.DataManager;
import org.jacobn99.skyblockgambit.Serialization.ItemStackSerialization;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomItemManager {
    JavaPlugin _mainPlugin;
    File _itemFile;
    List<CustomItems> customItemsList;
    List<String> requiredItemsList;
    private ItemStackSerialization _itemStackSerialization;
    private DataManager _dataManager;

    public CustomItemManager(JavaPlugin mainPlugin) {
        customItemsList = new ArrayList();
        _mainPlugin = mainPlugin;
        _itemFile = new File(_mainPlugin.getDataFolder(), "custom_items.json");
        _itemStackSerialization = new ItemStackSerialization();
        _dataManager = new DataManager();
        requiredItemsList = new ArrayList<>();
        requiredItemsList.add("PORTAL_OPENER");
        requiredItemsList.add("REDSTONE_KIT");
        requiredItemsList.add("TRADE_BOOST");
        requiredItemsList.add("RAGE_SPELL");
        requiredItemsList.add("KILL_SKULL");
        requiredItemsList.add("GENERATOR_CONSTRUCTOR");
        requiredItemsList.add("THE_SMASHER");
        requiredItemsList.add("NUKE_SHEEP");



    }

    public void LoadRequiredItems() {
        Set<String> itemNames = new HashSet<>();
        List<CustomItems> itemsList = GetCustomItemsList();
        for(CustomItems customItem : itemsList) {
            itemNames.add(customItem.getItemName());
        }

        if(!itemsList.isEmpty()) {
            for (String itemName : requiredItemsList) {
                if(!itemNames.contains(itemName)) {
                    ItemStack placeholder = new ItemStack(Material.DEAD_BUSH);
                    ItemMeta meta = placeholder.getItemMeta();
                    meta.setDisplayName(itemName + "(placeholder)");
                    placeholder.setItemMeta(meta);

                    AddCustomItem(placeholder, itemName);
                    Bukkit.broadcastMessage("Adding placeholder " + itemName);
                }
            }
        }
        else {
            ItemStack placeholder = new ItemStack(Material.DEAD_BUSH);
            ItemMeta meta = placeholder.getItemMeta();
            for (String itemName : requiredItemsList) {
                meta.setDisplayName(itemName + " (placeholder)");
                placeholder.setItemMeta(meta);

                AddCustomItem(placeholder, itemName);
                Bukkit.broadcastMessage("Adding placeholder " + itemName);
            }
        }
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
        else if(inputIndex == -1) {
            Bukkit.broadcastMessage("Item doesn't exist");
            return null;
        }
        else {
            index = inputIndex;
        }

        serializedItem = customItemsList.get(index).getItem();
        item = (ItemStack) _itemStackSerialization.Deserialize(serializedItem);

        return item;
    }
    public List<CustomItems> GetCustomItemsList() {
        UpdateCustomItemsList();
        return customItemsList;
    }
    public void ClearFile() {
        _itemFile.delete();
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
        customItemsList.add(new CustomItems(_itemStackSerialization.Serialize(p.getInventory().getItemInMainHand()), itemName));
        UpdateItemFile();
    }
    public void AddCustomItem(ItemStack item, String itemName) {
        UpdateCustomItemsList();
        customItemsList.add(new CustomItems(_itemStackSerialization.Serialize(item), itemName));
        UpdateItemFile();
    }

    public void SetCustomItem(Player p, int index, String name) {
        UpdateCustomItemsList();
        if(index > customItemsList.size() - 1) {
            Bukkit.broadcastMessage("Not in bounds of index");
        }
        else {
            customItemsList.set(index, new CustomItems(_itemStackSerialization.Serialize(p.getInventory().getItemInMainHand()), name));
            UpdateItemFile();
        }
    }

    public boolean AreEqual(ItemStack item1, ItemStack item2, boolean considerAmount) {
        if(item1 == null || item2 == null) { return false; }
        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

//        TestAreEqual(item1, item2, considerAmount);

        return LoreEqual(meta1,meta2) && meta1.getDisplayName().equals(meta2.getDisplayName()) &&
                    meta1.getItemFlags().equals(meta2.getItemFlags()) && item1.getType() == item2.getType() && (
                    !considerAmount || item1.getAmount() == item2.getAmount());
    }
    public boolean LoreEqual(ItemMeta meta1, ItemMeta meta2) {
        if(meta1.getLore() == null && meta2.getLore() == null) return true;
        else if(meta1.getLore() != null && meta2.getLore() != null) {
            return meta1.getLore().containsAll(meta2.getLore());
        }
        else return false;

    }

    private void TestLoreEqual(ItemMeta meta1, ItemMeta meta2) {
        Bukkit.broadcastMessage("-----Lore Test-----");
        Bukkit.broadcastMessage((meta1.getLore() == null) + ", " + (meta2.getLore() == null));
        if(meta1.getLore() != null && meta2.getLore() != null) {
            Bukkit.broadcastMessage("meta1 contains all meta2?: " + (meta1.getLore().containsAll(meta2.getLore())));
        }
        Bukkit.broadcastMessage("------------------");
    }


    public void TestAreEqual(ItemStack item1, ItemStack item2, boolean considerAmount) {
        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

        TestLoreEqual(meta1,meta2);
        Bukkit.broadcastMessage("tests: " + (item1 != null && item2 != null) + "\n" + LoreEqual(meta1,meta2) + "\n" + (meta1.getDisplayName().equals(meta2.getDisplayName())) + "\n" +
                (meta1.getItemFlags().equals(meta2.getItemFlags())) + "\n" + (item1.getType() == item2.getType()) + "\n(" +
                considerAmount + " || " + (item1.getAmount() == item2.getAmount()) + ")");
    }
}