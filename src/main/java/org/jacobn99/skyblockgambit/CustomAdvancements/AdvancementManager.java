package org.jacobn99.skyblockgambit.CustomAdvancements;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AdvancementManager {
    private Gson gson;
    private String advancementsPath;
    private String defaultConfiguration;
    private JavaPlugin _mainPlugin;
    public  List<CustomAdvancement> customAdvancements;
    public List<String> enabledAdvancementNames;
    int maxTasks;

    public AdvancementManager(JavaPlugin mainPlugin) {
        customAdvancements = new ArrayList<>();
        enabledAdvancementNames = new ArrayList<>();
        _mainPlugin = mainPlugin;
        gson = new Gson();
        maxTasks = 4;
        advancementsPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath().replace('\\', '/')
                + "/Spigot/void_world/datapacks/task_advancements/data/minecraft/advancement/";
        //defaultConfiguration = "{\"display\":{\"icon\":{\"id\":\"minecraft:spyglass\"},\"title\":\"Tasks\",\"description\":\"...\",\"frame\":\"task\",\"show_toast\":true,\"announce_to_chat\":false,\"hidden\":false},\"parent\":null,\"criteria\":{\"requirement\":{\"trigger\":\"minecraft:impossible\"}}}";

    }
    public List<String> GetEnabledTasks() {
        File file = new File(_mainPlugin.getDataFolder().getAbsolutePath() + "/tasks_list.json");
        List<String> advancementNames;

        try {
            if(file.exists() || file.length() != 0) {
                Reader reader = Files.newBufferedReader(file.toPath());
                Gson gson = new Gson();
                advancementNames = gson.fromJson(reader, List.class);
                //Bukkit.broadcastMessage(advancementNames.toString());
                reader.close();
                return advancementNames;

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public void RandomizeTasks() {
        int randomNumber;
        CustomAdvancement currentAdvancement;
        HashMap<Object,Object> parameterChanges = new HashMap<>();

        ArrayList<CustomAdvancement> availableAdvancements = new ArrayList();
        availableAdvancements.addAll(customAdvancements);
        CustomAdvancement parentAdvancement = null;

        for(int i = 0; i < maxTasks; i++) {
            Random rand = new Random();
            if(!(availableAdvancements.isEmpty())) {
                randomNumber = rand.nextInt(availableAdvancements.size());
                currentAdvancement = availableAdvancements.get(randomNumber);

                if (i == 0) {
                    parameterChanges.put("title", "Task " + (i + 1));
                    parameterChanges.put("parent", "minecraft:root");

                    ModifyAdvancement(currentAdvancement.GetFile(), parameterChanges);
                } else {
                    parameterChanges.put("title", "Task " + (i + 1));
                    parameterChanges.put("parent", "minecraft:" + parentAdvancement.GetFileName());
                    ModifyAdvancement(currentAdvancement.GetFile(), parameterChanges);
                }
                enabledAdvancementNames.add(currentAdvancement.GetFile().getName());
                parentAdvancement = currentAdvancement;
                parameterChanges.clear();
                availableAdvancements.remove(randomNumber);
            }
            else{
                Bukkit.broadcastMessage("Not enough tasks");
            }
        }
        CustomAdvancement enderdragon = new CustomAdvancement("enderdragon", new ItemStack(Material.DIAMOND, 10), customAdvancements);
        ModifyAdvancement(enderdragon.GetFile(), "parent", "minecraft:" + parentAdvancement.GetFileName());

        //ModifyAdvancement(enderdragon.GetFile(), "parent", "task_advancements:tasks/" + parentAdvancement.GetFileName());

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
        if(parameterChanges != null && !parameterChanges.isEmpty()) {
            try {
                reader = Files.newBufferedReader(file.toPath());
                Map<Object, Object> map = gson.fromJson(reader, Map.class);
                writer = Files.newBufferedWriter(file.toPath());

                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    if (parameterChanges.containsKey(entry.getKey())) {
                        entry.setValue(parameterChanges.get(entry.getKey()));
                        //Bukkit.broadcastMessage(file.getName() + ": " + entry.getKey() + " has been changed to " + parameterChanges.get(entry.getKey()));
                    }
                    else if (ContainsChar(String.valueOf(entry.getValue()), '{')) {
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
    public List<CustomAdvancement> GetCustomAdvancementList() {return customAdvancements;}
    public String GetDefaultConfiguration() {
        if(defaultConfiguration == null) {
            try {
                defaultConfiguration = Files.lines(Paths.get(advancementsPath + "defaultConfiguration.json")).collect(Collectors.joining("\n"));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return defaultConfiguration;
    }
    private boolean ContainsChar(String s, char targetCharacter) {
        for(char c : s.toCharArray()) {
            //Bukkit.broadcastMessage(String.valueOf(c));
            if(c == targetCharacter) {
                return true;
            }
        }
        return false;
    }
    public String GetAdvancementPath() {
        return advancementsPath;
    }
}