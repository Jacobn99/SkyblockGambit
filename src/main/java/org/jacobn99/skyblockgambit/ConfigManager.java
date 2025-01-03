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
import java.util.HashMap;
import java.util.List;

public class ConfigManager {
    JavaPlugin _mainPlugin;
//    File _itemFile;
//    List<CustomItems> customItemsList;
    public ConfigManager(JavaPlugin mainPlugin) {
        _mainPlugin = mainPlugin;
    }

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
