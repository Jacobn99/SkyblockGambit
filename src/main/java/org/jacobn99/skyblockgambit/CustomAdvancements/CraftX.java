package org.jacobn99.skyblockgambit.CustomAdvancements;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.DataManager;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Serialization.ItemStackSerialization;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CraftX implements AdvancementType {
    GameManager _gameManager;
    CustomAdvancement _advancement;
    private ItemStack _item;
    private AdvancementManager _advancementManager;
    private int iteration;
    private File _file;
    private JavaPlugin _mainPlugin;
    private ItemStackSerialization _itemStackSerialization;
    private DataManager _dataManager;
    public CraftX(AdvancementManager advancementManager, JavaPlugin mainPlugin) {
        _advancementManager = advancementManager;
        _mainPlugin = mainPlugin;
        _file = new File(_mainPlugin.getDataFolder().getAbsolutePath() + "/CraftXConfig.json");
        iteration = 0;
        _itemStackSerialization = new ItemStackSerialization();
        _dataManager = new DataManager();
        _item = GetItem();


    }
    public void UpdateDescription() {
        //Bukkit.broadcastMessage("item: " + item)
        _item = GetItem();
        //Bukkit.broadcastMessage("item: " + _item.getType().name());
        _advancementManager.ModifyAdvancement(new File(_advancementManager.GetAdvancementPath() + "/craft_item.json"), "description", "Craft " + _item.getType().name());
    }
    private ItemStack GetItem() {
        //Type listType = new TypeToken<List<String>>() {}.getType();
        if (_file.exists()) {
            List<Object> items = _dataManager.GetObjects(_file, _itemStackSerialization);
            if(!items.isEmpty()) {
                return (ItemStack) items.get(0);
            }

        }
        Bukkit.broadcastMessage("ERROR: No CraftX file!");
        return null;
    }

    public void WriteToCraftXFile() {
        Random rand = new Random();
        List<ItemStack> possibleItems = GetPossibleItems();
        //Bukkit.broadcastMessage("possibleItems: " + possibleItems);
        List<Object> writableContent = new ArrayList<>();
        int index = rand.nextInt(possibleItems.size());

        writableContent.add(possibleItems.get(index));

        _dataManager.WriteToFile(_file, writableContent, _itemStackSerialization);
    }
    private List<ItemStack> GetPossibleItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.CAKE));
        items.add(new ItemStack(Material.ANVIL));
        items.add(new ItemStack(Material.HAY_BLOCK));
        items.add(new ItemStack(Material.BLACKSTONE_SLAB));
        return items;
    }

    public void CraftXCheck(CraftItemEvent event) {
        ItemStack result = event.getInventory().getResult();
        Player p = (Player) event.getWhoClicked();

        // Bukkit.broadcastMessage("iteration: " + iteration);

        if (iteration == 0) {
            _advancement = _advancementManager.GetAdvancement("craft_item");
            _item = GetItem();
            iteration++;
        }
        Bukkit.broadcastMessage(_advancement.GetFileName() + ", " + _item.getType().name() + ", " + result.getType().name());
        if (_advancement != null && _item != null) {
            if (result.getType() == _item.getType()) {
                Bukkit.broadcastMessage("bazinga");
                _advancementManager.GrantTeamAdvancement(p, _advancement, true);
                //_advancement.GrantAdvancement(p, false);
            }
        }
        else {
            Bukkit.broadcastMessage("ERROR: No craft advancement");
        }
    }

    @Override
    public Material GetSymbol() {
        return Material.CRAFTING_TABLE;
    }

    @Override
    public String GetDescription() {
        return "Craft " + _item.getType().name().replace('_', ' ');
    }
}
