package org.jacobn99.skyblockgambit.CustomAdvancements;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.GameManager;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XStacks {
//    GameManager _gameManager;
//    CustomAdvancement _advancement;
//    private ItemStack _item;
//    private AdvancementManager _advancementManager;
//    private int iteration;
//    private File _file;
//    private JavaPlugin _mainPlugin;
//    private CustomItemManager _itemManager;
//    private Gson _gson;
//    private List<ItemStack> _possibleTtems;
//    public XStacks(AdvancementManager advancementManager, CustomItemManager itemManager, JavaPlugin mainPlugin) {
//        //_gameManager = gameManager;
//        _advancementManager = advancementManager;
//        _itemManager = itemManager;
//        _mainPlugin = mainPlugin;
//        _file = new File(_mainPlugin.getDataFolder().getAbsolutePath() + "/XStacksConfig.json");
//        iteration = 0;
//        //_item = new ItemStack(Material.OAK_PLANKS);
//        _gson = new Gson();
//
//
//    }
//    public void UpdateDescription() {
//        //Bukkit.broadcastMessage("item: " + item)
//        _item = GetItem();
//        Bukkit.broadcastMessage("item: " + _item.getType().name());
//        _advancementManager.ModifyAdvancement(new File(_advancementManager.GetAdvancementPath() + "/craft_item.json"), "description", "Craft " + _item.getType().name());
//    }
//    private ItemStack GetItem() {
//        //Type listType = new TypeToken<List<String>>() {}.getType();
//        if (_file.exists()) {
//            try {
//                Reader reader = Files.newBufferedReader(_file.toPath());
//                String serializedItem = _gson.fromJson(reader, String.class);
//                _item = _itemManager.DeserializeItem(serializedItem);
//                Bukkit.broadcastMessage("_item: " + _item);
//                return _item;
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        else {
//            Bukkit.broadcastMessage("ERROR: No CraftX file!");
//        }
//        return null;
//    }
////    public void LoadFile() {
////        if(!_file.exists()) {
////            _
////        }
////    }
//
//    public void WriteToCraftXFile() {
//        Random rand = new Random();
//        List<String> possibleItems = GetPossibleItems();
//        //Bukkit.broadcastMessage("possibleItems: " + possibleItems);
//
//        int index = rand.nextInt(possibleItems.size());
//
//
//        try {
//            Writer writer = Files.newBufferedWriter(_file.toPath());
//            _gson.toJson(possibleItems.get(index), writer);
//            //_gson.toJson(_itemManager.SerializeItem(_item), writer);
//            //Bukkit.broadcastMessage("Serialized Item: " + _itemManager.SerializeItem(_item));
//            //writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    private void InitializePossibleItems() {
//        List<ItemStack> possibleTtems = new ArrayList<>();
//        items.add(new ItemStack(Material.STONE));
//        items.add(new ItemStack(Material.BIRCH_PLANKS));
//    }
//    private List<String> GetPossibleItems() {
//        List<String> serializedItems = new ArrayList<>();
//        List<ItemStack> possibleTtems = new ArrayList<>();
//        items.add(new ItemStack(Material.STONE));
//        items.add(new ItemStack(Material.BIRCH_PLANKS));
//        for(ItemStack item : items) {
//            serializedItems.add(_itemManager.SerializeItem(item));
//        }
//        return serializedItems;
//    }
//
//    public void CraftXCheck(CraftItemEvent event) {
//        ItemStack result = event.getInventory().getResult();
//        Player p = (Player) event.getWhoClicked();
//
//        // Bukkit.broadcastMessage("iteration: " + iteration);
//
//        if (iteration == 0) {
//            _advancement = _advancementManager.GetAdvancement("craft_item");
//            //_item = GetItem();
//            iteration++;
//        }
//        if (_advancement != null) {
//            if (result.getType() == _item.getType()) {
//                Bukkit.broadcastMessage("bazinga");
//                _advancementManager.GrantTeamAdvancement(p, _advancement);
//                //_advancement.GrantAdvancement(p, false);
//            }
//        }
//        else {
//            Bukkit.broadcastMessage("ERROR: No craft advancement");
//        }
//    }
//}
}
