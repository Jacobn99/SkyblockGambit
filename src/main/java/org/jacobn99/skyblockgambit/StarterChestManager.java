package org.jacobn99.skyblockgambit;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StarterChestManager {
    File _chestFile;
    JavaPlugin _mainPlugin;
    List<StarterChest> starterChestList;
    CustomItemManager _itemManager;
    List<Chest> startChestBlocks;
    //List<String> serializedInventory;
    //List<ItemStack> chestInventory;
    ItemStack item1;
    ItemStack item2;
    //ItemStack[] chestInventory;
    //List<BlockData> _chestData;
    public StarterChestManager(JavaPlugin mainPlugin) {
        starterChestList = new ArrayList();
        _mainPlugin = mainPlugin;
        _chestFile = new File(_mainPlugin.getDataFolder(), "starting_chest.txt");
        _itemManager = new CustomItemManager(mainPlugin);
        startChestBlocks = new ArrayList();
        //chestInventory = new ArrayList<>();
        //serializedInventory = new ArrayList<>();
        //_chestData = new ArrayList<>();
    }

    public void LoadChestFile() {
        if(!_chestFile.exists()) {
            try {
                _chestFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//        item1 = new ItemStack(Material.DIAMOND, 30);
//        item2 = new ItemStack(Material.IRON_BARS, 15);
//        ItemStack[] chestInventory = new ItemStack[2];
//        chestInventory[0] = item1;
//        chestInventory[1] = item2;
//        serializedInventory = SerializeInventory(chestInventory);
//
//        if(!_chestFile.exists()) {
//            starterChestList.clear();
//
//            try {
//                _chestFile.createNewFile();
//                FileWriter writer = new FileWriter(_chestFile);
//                for(String s : serializedInventory) {
//                    writer.write(s + ",");
//                }
//                writer.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
    public void UpdateChestFile(ItemStack[] inventory) {
        LoadChestFile();
        List<String> serializedInventory = new ArrayList<>();
        for(ItemStack i : inventory) {
            serializedInventory.add(_itemManager.SerializeItem(i));
        }
        try {
            //_chestFile.createNewFile();
            FileWriter writer = new FileWriter(_chestFile);
            for(String s : serializedInventory) {
                writer.write(s + ",");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void UpdateStarterChest(Player p) {
            String stringContent;
            SetChestInventory(p);

            StringBuilder builder = new StringBuilder();
            try(BufferedReader buffer = new BufferedReader(new FileReader(_chestFile.getPath()))) {
                String string;
                while ((string = buffer.readLine()) != null) {

                    builder.append(string).append("\n");
                }
                stringContent = builder.toString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            char[] characters = stringContent.toCharArray();
            //Bukkit.broadcastMessage(String.valueOf(characters));
            List<Character> currentCharacters = new ArrayList<>();
            List<String> serializedItems = new ArrayList<>();
            int i = 0;


            for(char c : characters) {
                if(c == ',') {
                    char[] myCharArray = new char[currentCharacters.size()];
                    for(int ii = 0; ii < currentCharacters.size(); ii++) {
                        myCharArray[ii] = currentCharacters.get(ii);
                    }
                    serializedItems.add(String.copyValueOf(myCharArray));
                    currentCharacters.clear();
                    //Bukkit.broadcastMessage(String.copyValueOf(myCharArray) + "\n");
                }
                else {
                    currentCharacters.add(c);
                    //currentCharacters[i] = c;
                    //Bukkit.broadcastMessage(String.valueOf(currentCharacters[i]));
                    i++;
                }
            }
//            Bukkit.broadcastMessage(serializedItems.get(1) + "\n");
//            Bukkit.broadcastMessage("ACTUAL: " + _itemManager.SerializeItem(item2));

            for(String str : serializedItems) {
//              Bukkit.broadcastMessage(str + "\n");
                Bukkit.broadcastMessage(String.valueOf(_itemManager.DeserializeItem(str)));
            }
//            char[] myCharArray = new char[currentCharacters.size() - 1];
//            for(int ii = 0; ii < currentCharacters.size() - 1; ii++) {
//                myCharArray[ii] = currentCharacters.get(ii);
//            }
//            Bukkit.broadcastMessage(String.copyValueOf(myCharArray));


//            serializedInventory = builder.toString();
//            Bukkit.broadcastMessage(serializedInventory);
//        try {
////            FileReader reader = new FileReader(_chestFile);
////            Type type = new TypeToken<ArrayList<CustomItems>>(){}.getType();
////            Gson gson = new Gson();
////
////            starterChestList = gson.fromJson(reader, type);
////            //Bukkit.broadcastMessage(String.valueOf(starterChestList));
////            reader.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public List<String> SerializeInventory(ItemStack[] inventoryContents) {
        List<String> serializedInventory = new ArrayList();

        for(ItemStack i : inventoryContents) {
            serializedInventory.add(_itemManager.SerializeItem(i));
        }
        return serializedInventory;
    }
    public void SetChestInventory(Player p) {
        Location chestLoc = p.getLocation();
        chestLoc.setY(p.getLocation().getY() - 0.5);

        Chest chest = (Chest) chestLoc.getBlock().getState();
        UpdateChestFile(chest.getBlockInventory().getContents());

//        if(starterChestList.size() == 0) {
//            //starterChestList.add(new StarterChest(SerializeInventory(chest.getBlockInventory().getContents()), LocationToString(chest.getLocation())));
//        }
//        else {
//            starterChestList.set(0, new StarterChest(SerializeInventory(chest.getBlockInventory().getContents()), LocationToString(chest.getLocation())));
//        }
        //UpdateChestFile();
    }


//    private void UpdateChestFile() {
//        LoadChestFile();
//        try {
//            FileWriter writer = new FileWriter(_chestFile);
//            Gson gson = new Gson();
//
//            gson.toJson(starterChestList, writer);
//            writer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public void UpdateStarterChest() {
//        LoadChestFile();
//        try {
//            FileReader reader = new FileReader(_chestFile);
//            Type type = new TypeToken<ArrayList<CustomItems>>(){}.getType();
//            Gson gson = new Gson();
//
//            starterChestList = gson.fromJson(reader, type);
//            //Bukkit.broadcastMessage(String.valueOf(starterChestList));
//            reader.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public StarterChest CreateStarterChest(Location loc) {
//        loc.getBlock().setType(Material.CHEST);
//        Chest chest = (Chest)loc.getBlock().getState();
//        //List<String> inventory = GetStarterChestList().get(0).getInventory();
//
//        //StarterChest starterChest = new StarterChest(inventory, LocationToString(chest.getLocation()));
//        //Bukkit.broadcastMessage("Made it here");
//        //startChestBlocks.add(chest);
//        ApplyChestInventory();
//        return null;
//        //return starterChest;
//    }
//    public List<StarterChest> GetStarterChestList() {
//        UpdateStarterChest();
//        return starterChestList;
//    }
//    public List<String> LocationToString(Location loc) {
//        List<String> stringLocation = new ArrayList<>();
//        stringLocation.add(String.valueOf(loc.getX()));
//        stringLocation.add(String.valueOf(loc.getY()));
//        stringLocation.add(String.valueOf(loc.getZ()));
//        return stringLocation;
//    }
//    public Location StringToLocation(List<String> stringLocation) {
//        Location loc = new Location(Bukkit.getWorld("void_world"), Double.valueOf(stringLocation.get(0)),
//                Double.valueOf(stringLocation.get(1)), Double.valueOf(stringLocation.get(2)));
//
//        return loc;
//    }
//
//    public void ApplyChestInventory() {
//        for(StarterChest c : GetStarterChestList()) {
//            Location loc = StringToLocation(c.getStringLocation());
//            Chest chest = (Chest)loc.getBlock().getState();
//
//            chest.getBlockInventory().setContents(DeserializeInventory(c.getInventory()));
//            //c.getStringLocation()
//        }
//    }
//    public List<String> GetSerializedInventory() {
//
//        return GetStarterChestList().get(0).getInventory();
//    }
//
//    public List<String> SerializeInventory(ItemStack[] inventory) {
//        List<String> serializedInventory = new ArrayList();
//
//        for(ItemStack i : inventory) {
//            serializedInventory.add(_itemManager.SerializeItem(i));
//        }
//        return serializedInventory;
//    }
//    public ItemStack[] DeserializeInventory(List<String> serializedInventory) {
//        ItemStack[] deserializedInventory = new ItemStack[serializedInventory.size()];
//
//        int i = 0;
//        for(String s : serializedInventory) {
//            deserializedInventory[i] = _itemManager.DeserializeItem(s);
//            i++;
//        }
//        return deserializedInventory;
//    }
//    public void SetChestInventory(Player p) {
//        Location chestLoc = p.getLocation();
//        chestLoc.setY(p.getLocation().getY() - 0.5);
//
//        Chest chest = (Chest) chestLoc.getBlock().getState();
//        if(starterChestList.size() == 0) {
//            starterChestList.add(new StarterChest(SerializeInventory(chest.getBlockInventory().getContents()), LocationToString(chest.getLocation())));
//        }
//        else {
//            starterChestList.set(0, new StarterChest(SerializeInventory(chest.getBlockInventory().getContents()), LocationToString(chest.getLocation())));
//        }
//        UpdateChestFile();
//    }
//    public BlockData GetChestInventory() {
//        UpdateChestInventory();
//        return _chestData.get(0);
//    }

}
