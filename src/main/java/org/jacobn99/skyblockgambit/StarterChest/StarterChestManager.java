package org.jacobn99.skyblockgambit.StarterChest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.Serialization.ItemStackSerialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StarterChestManager {
    File _chestFile;
    JavaPlugin _mainPlugin;
    CustomItemManager _itemManager;
    ItemStackSerialization _itemStackSerialization;
    public StarterChestManager(JavaPlugin mainPlugin) {
        _mainPlugin = mainPlugin;
        _chestFile = new File(_mainPlugin.getDataFolder(), "starting_chest.txt");
        _itemManager = new CustomItemManager(mainPlugin);
        _itemStackSerialization = new ItemStackSerialization();
    }
    public ItemStack[] GetInventory() {
        return DeserializeInventory(GetSerializedInventory());
    }
    public void SetChestInventory(Player p) {
        Location chestLoc = p.getLocation();
        chestLoc.setY(p.getLocation().getY() - 0.5);

        Chest chest = (Chest) chestLoc.getBlock().getState();
        UpdateChestFile(chest.getBlockInventory().getContents());
        ReadChestFile();
    }
    public void UpdateChestFile(ItemStack[] inventory) {
        LoadChestFile();
        List<String> serializedInventory = new ArrayList<>();
        for(ItemStack i : inventory) {
            serializedInventory.add(_itemStackSerialization.Serialize(i));
        }
        try {
            FileWriter writer = new FileWriter(_chestFile);
            for(String s : serializedInventory) {
                writer.write(s + ",");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void ReadChestFile() {
        List<String> serializedInventory = GetSerializedInventory();

        for(ItemStack item : DeserializeInventory(serializedInventory)) {
            Bukkit.broadcastMessage(String.valueOf(item));
        }
    }

    private ItemStack[] DeserializeInventory(List<String> serializedInventory) {
        //List<String> serializedItems;
        //List<ItemStack> deserializedItems = new ArrayList<>();
        ItemStack[] deserializedItems = new ItemStack[27];

        //serializedItems = GetSerializedInventory();

        int i = 0;
        for(String s : serializedInventory) {
            deserializedItems[i] = (ItemStack) _itemStackSerialization.Deserialize(serializedInventory.get(i));
            //deserializedItems.add(_itemManager.DeserializeItem(s));
            i++;
        }
        return deserializedItems;
    }
    private List<String> GetSerializedInventory() {
        String stringContent;
        StringBuilder builder = new StringBuilder();
        List<String> serializedInventory;

        try(BufferedReader buffer = new BufferedReader(new FileReader(_chestFile.getPath()))) {
            String string;
            while ((string = buffer.readLine()) != null) {

                builder.append(string).append("\n");
            }
            stringContent = builder.toString();
            serializedInventory = SerializedDataToList(stringContent);
            return serializedInventory;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private List<String> SerializedDataToList(String serializedInventory) {
        List<String> serializedItems = new ArrayList<>();
        List<Character> currentCharacters = new ArrayList<>();
        char[] characters = serializedInventory.toCharArray();

        for(char c : characters) {
            if(c == ',') {
                char[] myCharArray = new char[currentCharacters.size()];
                for(int i = 0; i < currentCharacters.size(); i++) {
                    myCharArray[i] = currentCharacters.get(i);
                }
                serializedItems.add(String.copyValueOf(myCharArray));
                currentCharacters.clear();
            }
            else {
                currentCharacters.add(c);
            }
        }
        return serializedItems;
    }

    public void LoadChestFile() {
        if(!_chestFile.exists()) {
            try {
                _chestFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

// ================VERSION THAT IS BUILT WITH JSON==================
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
