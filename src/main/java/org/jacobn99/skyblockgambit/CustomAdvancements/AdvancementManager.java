package org.jacobn99.skyblockgambit.CustomAdvancements;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jacobn99.skyblockgambit.Team;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AdvancementManager {
    private Gson gson;
    public String advancementsPath;
    private String defaultConfiguration;
    private JavaPlugin _mainPlugin;
    public List<CustomAdvancement> customAdvancements;
    //public Map<Player, Integer> playerTasksMap;
    public List<String> futureEnabledAdvancementNames;
    //private List<String> _currentEnabledAdvancementNames;
    public List<CustomAdvancement> _currentEnabledAdvancements;

    private List<Team> _teams;
    //public CraftX craftX;
    //private CustomItemManager _customItemManager;
    private int _maxTasks;
    private boolean _advancementCompatible;
    File alternateTaskData;
    //public Inventory advancementInventory;

    public AdvancementManager(JavaPlugin mainPlugin, List<Team> teams) {
        customAdvancements = new ArrayList<>();
        futureEnabledAdvancementNames = new ArrayList<>();
        //_currentEnabledAdvancementNames = new ArrayList<>();
        _currentEnabledAdvancements = new ArrayList<>();
        //_customItemManager = customItemManager;
        _mainPlugin = mainPlugin;
        gson = new Gson();
        _maxTasks = 4;
        _teams = teams;
        _advancementCompatible = true;
        alternateTaskData = new File(_mainPlugin.getDataFolder() + "/alternate_task_data");
        //advancementInventory = Bukkit.createInventory(null, 9, "Tasks");
        String worldFilePath = Bukkit.getWorld("void_world").getWorldFolder().getAbsolutePath();
        advancementsPath = worldFilePath.replace('\\', '/')
                + "/datapacks/task_advancements/data/minecraft/advancement/";
    }
    public void GrantRootAdvancement(Player p) {
        String command = "advancement grant " + p.getName() + " only minecraft:root";
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }
    public void ClearTaskParents() {
        if(_advancementCompatible) {
            if (GetCustomAdvancementList() != null) {
                for (CustomAdvancement advancement : GetCustomAdvancementList()) {
                    Bukkit.broadcastMessage("Modifying " + advancement.GetFileName());
                    ModifyAdvancement(advancement.GetFile(), "parent", "null");
                }
            } else {
                Bukkit.broadcastMessage("GetCustomAdvancementList() is null");
            }
        }
    }
    public List<CustomAdvancement> GetCurrentEnabledTasks() {
        if(_currentEnabledAdvancements.isEmpty()) {
            int i = 0;
            List<String> currentAdvancementNames = GetCurrentEnabledTaskNames();
            for (String name : currentAdvancementNames) {
                CustomAdvancement advancement = GetAdvancement(name);
                if(advancement != null) {
                    advancement.SetOrder(i);
                    _currentEnabledAdvancements.add(advancement);
                }
            }
            //return _currentEnabledAdvancements;
        }
        return _currentEnabledAdvancements;
    }
    public List<String> GetCurrentEnabledTaskNames() {
        File file = new File(_mainPlugin.getDataFolder().getAbsolutePath() + "/tasks_list.json");
        Gson gson = new Gson();
        List<String> currentAdvancementNames;
        try {
            Type listOfMyClassObject = new TypeToken<ArrayList<String>>() {
            }.getType();
            Reader reader = Files.newBufferedReader(file.toPath());
            currentAdvancementNames = gson.fromJson(reader, listOfMyClassObject);
            reader.close();

            gson = null;
            return currentAdvancementNames;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void GrantTeamAdvancement(Player p, CustomAdvancement a, boolean isConditional) {
        if(GetCurrentEnabledTasks().contains(a)) {
            for (Team team : _teams) {
                if (team.GetMembers().contains(p) && a != null) {
                    for (Player player : team.GetMembers()) {
                        Bukkit.broadcastMessage("Trying to grant a team an advancement");

                        if(a.GrantAdvancement(player, isConditional)) {
                            team.AddFinishedTask(a);
                        }
                        return;
                    }
                }
            }
            Bukkit.broadcastMessage("You must join a team first!");
        }
    }

    public void RandomizeTasks() {
        int randomNumber;
        CustomAdvancement currentAdvancement = null;
        CustomAdvancement firstAdvancement = null;
        HashMap<Object, Object> parameterChanges = new HashMap<>();
        ArrayList<CustomAdvancement> availableAdvancements = new ArrayList();
        List<CustomAdvancement> usedAdvancements = new ArrayList<>();
        CustomAdvancement parentAdvancement = null;

        availableAdvancements.addAll(customAdvancements);
        availableAdvancements.remove(GetAdvancement("kill_enderdragon"));


        if(!customAdvancements.isEmpty()) {
            for (int i = 0; i < _maxTasks - 1; i++) {
                Random rand = new Random();
                if (!(availableAdvancements.isEmpty())) {
                    randomNumber = rand.nextInt(availableAdvancements.size());
                    currentAdvancement = availableAdvancements.get(randomNumber);
                    if(currentAdvancement.GetFileName().equalsIgnoreCase("reach_level") && i != 0) {
                        firstAdvancement = usedAdvancements.get(0);
                        usedAdvancements.set(0, currentAdvancement);
                        usedAdvancements.add(firstAdvancement);
                    }
                    else {
                        usedAdvancements.add(currentAdvancement);
                    }

                    availableAdvancements.remove(randomNumber);
                } else {
                    Bukkit.broadcastMessage("Not enough tasks");
                }
            }
            int index = 0;
            for(CustomAdvancement a : usedAdvancements) {
                Bukkit.broadcastMessage("Current: " + a.GetFileName());
                if(parentAdvancement != null ) {
                    Bukkit.broadcastMessage("parent: " + parentAdvancement.GetFileName());
                }
                if(index == 0) {
                    parameterChanges.put("parent", "minecraft:root");
                }
                else {
                    parameterChanges.put("parent", "minecraft:" + parentAdvancement.GetFileName());
                }
                parameterChanges.put("title", a.GetAdvancementName());
                a.SetParentAdvancement(parentAdvancement);
                ModifyAdvancement(a.GetFile(), parameterChanges);

                parentAdvancement = a;
                futureEnabledAdvancementNames.add(a.GetFileName());

                parameterChanges.clear();
                index++;
            }
            CustomAdvancement _kill_enderdragon = GetAdvancement("kill_enderdragon");
            ModifyAdvancement(_kill_enderdragon.GetFile(), "parent", "minecraft:" + parentAdvancement.GetFileName());
            ModifyAdvancement(_kill_enderdragon.GetFile(), "title", _kill_enderdragon.GetAdvancementName());
        }
        else {
            Bukkit.broadcastMessage("Type /start to get new tasks");
        }
    }

    public void ModifyAdvancement(File file, HashMap<Object, Object> parameterChanges) {
        if(_advancementCompatible) {
            ChangeAdvancementFile(file, parameterChanges);
        }
    }

    public void ModifyAdvancement(File file, Object key, Object newValue) {
        HashMap<Object, Object> parameterChanges = new HashMap<>();
        parameterChanges.put(key, newValue);

        if(_advancementCompatible) {
            ChangeAdvancementFile(file, parameterChanges);
        }
    }
    private void ChangeAdvancementFile(File file, HashMap<Object, Object> parameterChanges) {
        Writer writer;
        Reader reader;
        if(_advancementCompatible) {
            if (parameterChanges != null && !parameterChanges.isEmpty()) {
                try {
                    reader = Files.newBufferedReader(file.toPath());
                    Map<Object, Object> map = gson.fromJson(reader, Map.class);
                    writer = Files.newBufferedWriter(file.toPath());

                    for (Map.Entry<Object, Object> entry : map.entrySet()) {
                        if (parameterChanges.containsKey(entry.getKey())) {
                            entry.setValue(parameterChanges.get(entry.getKey()));
                        } else if (ContainsChar(String.valueOf(entry.getValue()), '{')) {
                            Map<Object, Object> innerMap = new Gson().fromJson(new Gson().toJson(entry.getValue()), Map.class);

                            for (Map.Entry<Object, Object> innerEntry : innerMap.entrySet()) {
                                if (parameterChanges.containsKey(innerEntry.getKey())) {
                                    innerEntry.setValue(parameterChanges.get(innerEntry.getKey()));
                                    map.replace(entry.getKey(), innerMap);
                                }
                            }
                        }
                    }
                    gson.toJson(map, writer);
                    writer.close();
                    reader.close();
                } catch (IllegalStateException | JsonSyntaxException | IOException exception) {
                    Bukkit.broadcastMessage("error");
                    exception.printStackTrace();
                }
            }
        }
        else {
            ModifyAlternativeGUI(file.getName(), parameterChanges);
        }
    }
    private void ModifyAlternativeGUI(String advancementName, HashMap<Object, Object> parameterChanges) {
        CustomAdvancement advancement = GetAdvancement(advancementName);
        List<String> lore = new ArrayList<>();
        int order = advancement.GetOrder();

        for (Team team : _teams) {
            ItemStack item = team.tasksInventory.getItem(order);
            ItemMeta meta = item.getItemMeta();
            for (Object key : parameterChanges.keySet()) {
                String s = (String) key;

                switch (s) {
                    case "title":
                        meta.setDisplayName((String) parameterChanges.get(key));
                        break;
                    case "description":
                        lore.add((String) parameterChanges.get(key));
                        meta.setLore(lore);
                        break;
                    default:
                        break;
                }
                lore.clear();
            }
            item.setItemMeta(meta);
            team.tasksInventory.setItem(order, item);

            item = null;
            meta = null;
        }
    }

    public List<CustomAdvancement> GetCustomAdvancementList() {
        return customAdvancements;
    }

    public String GetDefaultConfiguration() {
        if (defaultConfiguration == null) {
            try {
                defaultConfiguration = Files.lines(Paths.get(advancementsPath + "default_configuration.json")).collect(Collectors.joining("\n"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultConfiguration;
    }

    private boolean ContainsChar(String s, char targetCharacter) {
        for (char c : s.toCharArray()) {
            //Bukkit.broadcastMessage(String.valueOf(c));
            if (c == targetCharacter) {
                return true;
            }
        }
        return false;
    }

    public CustomAdvancement GetAdvancement(String fileName) {
        List<CustomAdvancement> advancementsList = GetCustomAdvancementList();
        for (CustomAdvancement a : advancementsList) {
            //Bukkit.broadcastMessage(fileName + ", " + a.GetFileName());
            if (a.GetFileName().equalsIgnoreCase(fileName)) {
                //Bukkit.broadcastMessage("worked");
                return a;
            }
        }
        return null;
    }
    //public Inventory CreateTaskInventory(Player p) {
    public void InitializeTaskInventory(Inventory inventory) {
        int i = 0;
        List<String> lore = new ArrayList<>();

        for(CustomAdvancement a : GetCurrentEnabledTasks()) {
            ItemStack item = new ItemStack(a.GetType().GetSymbol());
            ItemMeta meta = item.getItemMeta();

            lore.add(a.GetType().GetDescription());
            meta.setDisplayName(a.GetFileName().toLowerCase().replace('_', ' '));
            meta.setLore(lore);
            item.setItemMeta(meta);
            Bukkit.broadcastMessage("Item: " + item);
            Bukkit.broadcastMessage("Lore: " + lore.get(0));
            Bukkit.broadcastMessage("Display name: " + a.GetFileName().toLowerCase().replace('_', ' '));
            inventory.setItem(i, item);

            item = null;
            meta = null;
            lore.clear();
            i++;
        }
        //return inventory;
    }
    public String GetAdvancementPath() {
        return advancementsPath;
    }

    public int GetMaxTasks() {
        return _maxTasks;
    }
}