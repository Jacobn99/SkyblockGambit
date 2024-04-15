package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    JavaPlugin _mainPlugin;
    public ConfigManager(JavaPlugin mainPlugin) {
        _mainPlugin = mainPlugin;
    }

    public List<String> GetArguments(String path) {
        String villager1 = _mainPlugin.getConfig().getString(path);
        char[] villager1Char = villager1.toCharArray();
        int i = 0;
        List<Character> currentWord = new ArrayList();
        List<String> arguments = new ArrayList();


//        Bukkit.broadcastMessage(villager1);
//        Bukkit.broadcastMessage("length: " + villager1Char.length);
        for(char c : villager1Char) {
            if(c != ' ' && c != ',') {
                currentWord.add(c);
            }
            if(c == ',' || i == villager1Char.length - 1) {
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
