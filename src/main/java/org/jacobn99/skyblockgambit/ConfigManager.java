package org.jacobn99.skyblockgambit;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    JavaPlugin _mainPlugin;
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


//        Bukkit.broadcastMessage(villager1);
//        Bukkit.broadcastMessage("length: " + villager1Char.length);
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
