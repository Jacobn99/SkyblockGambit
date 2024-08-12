package org.jacobn99.skyblockgambit.CustomAdvancements;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.DataManager;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Serialization.ItemStackSerialization;
import org.jacobn99.skyblockgambit.Team;

import java.io.File;
import java.util.*;

public class XStacks {
    GameManager _gameManager;
    CustomAdvancement _advancement;
    private Map<ItemStack, Integer> _itemMap;
    private ItemStack _item;
    private AdvancementManager _advancementManager;
    private int iteration;
    private File _dataFile;
    private JavaPlugin _mainPlugin;
    private ItemStackSerialization _itemStackSerialization;
    private DataManager _dataManager;
    private List<ItemStack> _possibleItems;

    public XStacks(AdvancementManager advancementManager, GameManager gameManager, JavaPlugin mainPlugin) {
        _advancementManager = advancementManager;
        //_itemManager = itemManager;
        _mainPlugin = mainPlugin;
        _gameManager = gameManager;
        _dataFile = new File(_mainPlugin.getDataFolder().getAbsolutePath() + "/XStacksData.json");
        iteration = 0;
        _itemStackSerialization = new ItemStackSerialization();
        _dataManager = new DataManager();
        _possibleItems = new ArrayList<>();
        _itemMap = new HashMap<>();
        //_item = GetItem();
        InitializeItem();

    }
    private void InitializePossibleItems() {
        _possibleItems.add(new ItemStack(Material.STONE));
        _possibleItems.add(new ItemStack(Material.POTATO));
        _possibleItems.add(new ItemStack(Material.WHEAT));
        _possibleItems.add(new ItemStack(Material.BAKED_POTATO));
        _possibleItems.add(new ItemStack(Material.BREAD));
    }
    public void UpdateDescription() {
        //Bukkit.broadcastMessage("item: " + item)
        //_item = GetItem();
        //Bukkit.broadcastMessage("item: " + _item.getType().name());
        _advancementManager.ModifyAdvancement(new File(_advancementManager.GetAdvancementPath() + "/x_stacks.json"), "description", "Get " + _itemMap.get(_item) + " " + _item.getType().name());
    }
    private void InitializeItem() {
        InitializePossibleItems();

        if (_dataFile.exists()) {
            List<String> items = _dataManager.GetSerializedObjects(_dataFile);
            if (items != null) {
                if (items.size() > 1) {
                    _item = (ItemStack) _itemStackSerialization.Deserialize(items.get(0));
                    _itemMap.put(_item, Integer.valueOf(items.get(1)));
                }
            }
        }
        else {
            Bukkit.broadcastMessage("ERROR: No XStacks file!");
        }
    }

    public void WriteToXStacksFile() {
        Random rand = new Random();
        List<ItemStack> possibleItems = GetPossibleItems();
        //Bukkit.broadcastMessage("possibleItems: " + possibleItems);
        List<Object> writableContent = new ArrayList<>();
        int index = rand.nextInt(possibleItems.size());
        _dataFile.delete();

        writableContent.add(possibleItems.get(index));
        writableContent.add(rand.nextInt(200));

        int i = 0;
        for(Object o : writableContent) {
            if(i == 0) {
                //Bukkit.broadcastMessage(i + ": " + o.getClass().getName());
                _dataManager.AddObjectToFile(_dataFile, o, _itemStackSerialization);
            }
            else if(i==1) {
                _dataManager.AddObjectToFile(_dataFile, o, null);
            }
            i++;
        }
        //_dataManager.WriteToFile(_dataFile, writableContent, _itemStackSerialization);
    }
    private List<ItemStack> GetPossibleItems() {
        return _possibleItems;
    }
    public void XStacksCheck(InventoryClickEvent event) {
        int count = 0;
        Player p = (Player) event.getWhoClicked();
        //Bukkit.broadcastMessage(p.getName());
        Inventory inventory = event.getInventory();
        Team team = _gameManager.FindPlayerTeam(p);
        //Bukkit.broadcastMessage(team.GetTeamColor());

        if(team != null) {
            if(inventory == team.killsInventory) {
                for (ItemStack item : inventory.getContents()) {
                    if (item != null) {
                        if (item.getType().equals(_item.getType())) {
                            count += item.getAmount();
                        }
                    }
                }
            }
        }
        if(count >= _itemMap.get(_item)) {
            _advancement = _advancementManager.GetAdvancement("x_stacks");
            _advancementManager.GrantTeamAdvancement(p, _advancement, true);
        }
    }
}
