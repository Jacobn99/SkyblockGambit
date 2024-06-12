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

    public CustomAdvancement(String advancementName, ItemStack reward, List<CustomAdvancement> customAdvancementList) {
        InitializeVariables(advancementName, customAdvancementList);
        _reward = new ItemStack[1];
        _reward[0] = reward;
    }
    public CustomAdvancement(String advancementName, ItemStack[] reward, List<CustomAdvancement> customAdvancementList) {
        InitializeVariables(advancementName, customAdvancementList);
        _reward = reward;
    }
    private void InitializeVariables(String advancementName, List<CustomAdvancement> customAdvancementList) {
        _customAdvancementList = customAdvancementList;
        _advancementsPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath().replace('\\', '/')
                + "/Spigot/void_world/datapacks/task_advancements/data/task_advancements/advancements/tasks/";
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
    public void GrantAdvancement(Player p) {
        if(!_playerList.contains(p)) {
            String command = "advancement grant " + p.getName() + " only task_advancements:tasks/" + this.GetFileName();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            for (ItemStack item : _reward) {
                p.getWorld().dropItem(p.getLocation(), item);
            }
            _playerList.add(p);
            //p.addScoreboardTag(this.GetFileName());
        }
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
