package org.jacobn99.skyblockgambit.CustomAdvancements;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
//        craftX = new CraftX(this, _customItemManager, _mainPlugin);
        //playerTasksMap = new HashMap<>();
        String worldFilePath = Bukkit.getWorld("void_world").getWorldFolder().getAbsolutePath();
        //String worldFilePath = Bukkit;

        advancementsPath = worldFilePath.replace('\\', '/')
                + "/datapacks/task_advancements/data/minecraft/advancement/";
        if(advancementsPath.toCharArray()[0] != 'C') {
            advancementsPath = "C:" + advancementsPath;
        }
//        advancementsPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath().replace('\\', '/')
//                + "/Spigot/void_world/datapacks/task_advancements/data/minecraft/advancement/";
        //defaultConfiguration = "{\"display\":{\"icon\":{\"id\":\"minecraft:spyglass\"},\"title\":\"Tasks\",\"description\":\"...\",\"frame\":\"task\",\"show_toast\":true,\"announce_to_chat\":false,\"hidden\":false},\"parent\":null,\"criteria\":{\"requirement\":{\"trigger\":\"minecraft:impossible\"}}}";

    }
    public void GrantRootAdvancement(Player p) {
        String command = "advancement grant " + p.getName() + " only minecraft:root";
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }
    public void ClearTaskParents() {
        if(GetCustomAdvancementList() != null) {
            for (CustomAdvancement advancement : GetCustomAdvancementList()) {
                Bukkit.broadcastMessage("Modifying " + advancement.GetFileName());
                ModifyAdvancement(advancement.GetFile(), "parent", "null");
            }
        }
        else {
            Bukkit.broadcastMessage("GetCustomAdvancementList() is null");
        }
    }
    public List<CustomAdvancement> GetCurrentEnabledTasks() {
        if(_currentEnabledAdvancements.isEmpty()) {
            List<String> currentAdvancementNames = GetCurrentEnabledTaskNames();
            for (String name : currentAdvancementNames) {
                CustomAdvancement advancement = GetAdvancement(name);
                if(advancement != null) {
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

    public void GrantTeamAdvancement(Player p, CustomAdvancement a) {
        if(GetCurrentEnabledTasks().contains(a)) {
            for (Team team : _teams) {
                if (team.GetMembers().contains(p) && a != null) {
                    for (Player player : team.GetMembers()) {
                        Bukkit.broadcastMessage("Trying to grant a team an advancement");

                        if(a.GrantAdvancement(player, false)) {
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
        CustomAdvancement currentAdvancement;
        CustomAdvancement firstAdvancement = null;
        HashMap<Object, Object> parameterChanges = new HashMap<>();

        ArrayList<CustomAdvancement> availableAdvancements = new ArrayList();
        availableAdvancements.addAll(customAdvancements);
        availableAdvancements.remove(GetAdvancement("kill_enderdragon"));
        CustomAdvancement parentAdvancement = null;

        if(!customAdvancements.isEmpty()) {
            for (int i = 0; i < _maxTasks - 1; i++) {
                Random rand = new Random();
                //Bukkit.broadcastMessage("availableAdvancements size: " + availableAdvancements.size());
                if (!(availableAdvancements.isEmpty())) {
                    randomNumber = rand.nextInt(availableAdvancements.size());
                    currentAdvancement = availableAdvancements.get(randomNumber);

                    if (i == 0) {
                        parameterChanges.put("title", currentAdvancement.GetFileName());
                        parameterChanges.put("parent", "minecraft:root");
                        ModifyAdvancement(currentAdvancement.GetFile(), parameterChanges);
                        firstAdvancement = currentAdvancement;
                    } else {
                        //parameterChanges.put("title", "Task " + (i + 1));
                        if(currentAdvancement.GetFileName().equalsIgnoreCase( "reach_level")) {
                            parameterChanges.put("parent", "minecraft:root");
                            ModifyAdvancement(firstAdvancement.GetFile(), "parent", "minecraft:" + parentAdvancement.GetFileName());

                        }
                        else {
                            parameterChanges.put("parent", "minecraft:" + parentAdvancement.GetFileName());
                        }
                        parameterChanges.put("title", currentAdvancement.GetFileName());
                        //parameterChanges.put("parent", "minecraft:" + parentAdvancement.GetFileName());
                        currentAdvancement.SetParentAdvancement(parentAdvancement);
                        ModifyAdvancement(currentAdvancement.GetFile(), parameterChanges);
                    }
                    Bukkit.broadcastMessage("Current: " + currentAdvancement.GetFileName());

                    futureEnabledAdvancementNames.add(currentAdvancement.GetFileName());
                    parentAdvancement = currentAdvancement;
                    parameterChanges.clear();
                    availableAdvancements.remove(randomNumber);
                } else {
                    Bukkit.broadcastMessage("Not enough tasks");
                }
            }
            //CustomAdvancement kill_enderdragon = new CustomAdvancement("kill_enderdragon", new ItemStack(Material.DIAMOND), this);
            CustomAdvancement _kill_enderdragon = GetAdvancement("kill_enderdragon");
            ModifyAdvancement(_kill_enderdragon.GetFile(), "parent", "minecraft:" + parentAdvancement.GetFileName());
            ModifyAdvancement(_kill_enderdragon.GetFile(), "title", _kill_enderdragon.GetFileName());

            //kill_enderdragon = null;

            //ModifyAdvancement(enderdragon.GetFile(), "parent", "task_advancements:tasks/" + parentAdvancement.GetFileName());
        }
        else {
            Bukkit.broadcastMessage("Type /start to get new tasks");
        }
    }

    public void ModifyAdvancement(File file, HashMap<Object, Object> parameterChanges) {
        ChangeAdvancementFile(file, parameterChanges);
    }

    public void ModifyAdvancement(File file, Object key, Object newValue) {
        HashMap<Object, Object> parameterChanges = new HashMap<>();
        parameterChanges.put(key, newValue);

        ChangeAdvancementFile(file, parameterChanges);
    }

    private void ChangeAdvancementFile(File file, HashMap<Object, Object> parameterChanges) {
        Writer writer;
        Reader reader;
        if (parameterChanges != null && !parameterChanges.isEmpty()) {
            try {
                reader = Files.newBufferedReader(file.toPath());
                Map<Object, Object> map = gson.fromJson(reader, Map.class);
                writer = Files.newBufferedWriter(file.toPath());

                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    if (parameterChanges.containsKey(entry.getKey())) {
                        entry.setValue(parameterChanges.get(entry.getKey()));
                        //Bukkit.broadcastMessage(file.getName() + ": " + entry.getKey() + " has been changed to " + parameterChanges.get(entry.getKey()));
                    } else if (ContainsChar(String.valueOf(entry.getValue()), '{')) {
                        Map<Object, Object> innerMap = new Gson().fromJson(new Gson().toJson(entry.getValue()), Map.class);

                        for (Map.Entry<Object, Object> innerEntry : innerMap.entrySet()) {
                            if (parameterChanges.containsKey(innerEntry.getKey())) {
                                innerEntry.setValue(parameterChanges.get(innerEntry.getKey()));
                                map.replace(entry.getKey(), innerMap);
                                //Bukkit.broadcastMessage(file.getName() + ": " + innerEntry.getKey() + " has been changed to " + parameterChanges.get(innerEntry.getKey()));
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

    public String GetAdvancementPath() {
        return advancementsPath;
    }

    public int GetMaxTasks() {
        return _maxTasks;
    }

}