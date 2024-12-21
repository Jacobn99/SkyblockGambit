package org.jacobn99.skyblockgambit.CustomAdvancements;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CustomAdvancement {
    String _advancementsPath;
    ItemStack _reward[];
    File _file;
    List<CustomAdvancement> _customAdvancementList;
    Set<Player> _playerList;
    CustomAdvancement _parentAdvancement;
    AdvancementManager _advancementManager;
    AdvancementType _type;
    private int _order;
    //private Material _symbol;

    public CustomAdvancement(String advancementName, AdvancementType type, ItemStack reward, AdvancementManager advancementManager) {
        _advancementManager = advancementManager;
        InitializeVariables(advancementName, _advancementManager.GetCustomAdvancementList());
        _reward = new ItemStack[1];
        _reward[0] = reward;
        _parentAdvancement = null;
        _type = type;
        //Bukkit.broadcastMessage(advancementName + " has been created");
    }
    public CustomAdvancement(String advancementName, AdvancementType type, ItemStack[] reward, List<CustomAdvancement> customAdvancementList) {
        InitializeVariables(advancementName, customAdvancementList);
        _reward = reward;
        _type = type;

    }
    private void InitializeVariables(String advancementName, List<CustomAdvancement> customAdvancementList) {
        _customAdvancementList = customAdvancementList;
        _advancementsPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath().replace('\\', '/')
                + "/LocalHost/void_world/datapacks/task_advancements/data/minecraft/advancement/";
        _file = new File( _advancementsPath + advancementName + ".json");
        _customAdvancementList.add(this);
        _playerList = new HashSet<>();
    }
    public String GetFileName() {
        String fileName = _file.getName();
        StringBuilder builder = new StringBuilder();

        for(char c : fileName.toCharArray()) {
            if(c == '.') {
                break;
            }
            else {
                builder.append(c);
            }
        }
        fileName = builder.toString();
        return fileName;
    }
    public File GetFile() {return _file;}
    public void SetFile(File _file) {this._file = _file;}
    public ItemStack[] GetReward() {return _reward;}

    public void SetReward(ItemStack _reward[]) {this._reward = _reward;}

    public CustomAdvancement GetParentAdvancement() {
        return _parentAdvancement;
    }

    public void SetParentAdvancement(CustomAdvancement _parentAdvancement) {
        this._parentAdvancement = _parentAdvancement;
    }

    public Set<Player> GetPlayerList() {
        return _playerList;
    }

    public void SetPlayerList(Set<Player> _playerList) {
        this._playerList = _playerList;
    }
    public AdvancementType GetType() {
        return _type;
    }

    public boolean CheckPrequisiteAdvancement(Player p) {
        if(_parentAdvancement != null) {
            if(_parentAdvancement.GetPlayerList().contains(p)) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }
    public int GetOrder() {
        return _order;
    }
    public void SetOrder(int o) {
        _order = o;
    }

    public boolean GrantAdvancement(Player p, boolean isConditional) {
        //Bukkit.broadcastMessage("Player list: " + _playerList + " isConditional: " + isConditional);
        if(_advancementManager.GetCurrentEnabledTasks().contains(this)) {
            if ((!_playerList.contains(p) && CheckPrequisiteAdvancement(p)) || (!_playerList.contains(p) && !isConditional)) {
                //Bukkit.broadcastMessage("Got here");
                String command = "advancement grant " + p.getName() + " only minecraft:" + this.GetFileName();
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                for (ItemStack item : _reward) {
                    p.getWorld().dropItem(p.getLocation(), item);
                }
                _playerList.add(p);
                return true;
                //p.addScoreboardTag(this.GetFileName());
            }
        }
        return false;
    }

    public void RemoveAdvancement(Player p, String advancement) {
        if(!_playerList.contains(p)) {
            String command = "advancement revoke " + p.getName() + " only jacobs_epic_advancements:" + advancement;
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            _playerList.remove(p);
        }
    }
    public void LoadFile(String defaultConfig) {
        try {
            if(!_file.exists() || _file.length() == 0) {
                FileWriter writer = new FileWriter(_file);
                writer.write(defaultConfig);
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
