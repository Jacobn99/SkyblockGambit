package org.jacobn99.skyblockgambit;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomAdvancements.AdvancementManager;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.CustomItems.CustomItems;
import org.jacobn99.skyblockgambit.CustomWorlds.CustomWorld;
import org.jacobn99.skyblockgambit.CustomWorlds.WorldCopier;
import org.jacobn99.skyblockgambit.CustomWorlds.WorldManager;
import org.jacobn99.skyblockgambit.Portals.Portal;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.StarterChest.StarterChestManager;

import java.io.File;

public class CommandExecuter implements CommandExecutor {
    private JavaPlugin _mainPlugin;
    private GameManager _gameManager;
    private CustomItemManager _itemManager;
    //private PortalManager _portalManager;
    private StarterChestManager _chestManager;
    private AdvancementManager _advancementManager;
    WorldCopier _worldCopier;
    WorldManager _worldManager;
    PortalManager _portalManager;
    //ProcessManager _processManager;
    public CommandExecuter(JavaPlugin mainPlugin, GameManager gameManager) {
        _mainPlugin = mainPlugin;
        _gameManager = gameManager;
        _itemManager = new CustomItemManager(_mainPlugin);
        //_portalManager = new PortalManager();
        _chestManager = new StarterChestManager(_mainPlugin);
        _advancementManager = new AdvancementManager(_mainPlugin);
        _portalManager = new PortalManager(_gameManager);
        //_worldCopier = new WorldCopier(_mainPlugin, _gameManager.processes, _processManager);
        //_processManager = new ProcessManager();
        //_worldManager = new WorldManager(_mainPlugin, _gameManager, _portalManager, _processManager);
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (IsCommandValid(sender)) {
            Player p = (Player) sender;

            if (label.equalsIgnoreCase("start")) {
                sender.sendMessage(ChatColor.RED + "start");
                _gameManager.Start();
                return true;
            } if (label.equalsIgnoreCase("end")) {
                sender.sendMessage(ChatColor.RED + "end");
                _gameManager.EndGame();
                //_gameManager = new GameManager(_mainPlugin);


                _gameManager.blueTeam.add(p);
                _gameManager.redTeam.clear();
                Bukkit.broadcastMessage("red team: " + _gameManager.GetRedTeamList());
                return true;
            } else if (label.equalsIgnoreCase("debug")) {
                sender.sendMessage(ChatColor.RED + "debug");
                _gameManager.InitializeTasks();
//                File file = new File( _mainPlugin.getDataFolder().getAbsolutePath() + "/output.json");
//                _worldManager.BuildWorld(_gameManager.redWorld, file, _processManager);
                //_worldCopier.DuplicateLand(p.getLocation(), file);

                //_worldCopier.PasteChunkPiece(_worldCopier.GetChunkPieceData(file.getAbsolutePath()), 0, p.getLocation());
//
                //_worldCopier.DuplicateLand(p.getLocation(), file);
                //Bukkit.broadcast  Message("latest execution time: " + _processManager.GetLatestExecutionTime(_gameManager.processes));

                for(Portal portal : _gameManager.portals) {
                    Bukkit.broadcastMessage("Portal Loc: " + portal.GetPortalLocation());
                }
                for(CustomWorld customWorld : _gameManager.customWorlds) {
                    Bukkit.broadcastMessage("Reference Corner: " + customWorld.GetReferenceCorner());
                }
//                _worldCopier.DuplicateLand(20, 20, new Location(Bukkit.getWorld("void_world"),-13, 75, -53),
//                        new Location(Bukkit.getWorld("void_world"), 193, 75, 482));
                //_gameManager.InitializeTasks();
                //Bukkit.broadcastMessage(_advancementManager.GetEnabledTasks().toString());

//                HashMap<Object, Object> parameterChanges = new HashMap<>();
//
//                parameterChanges.put("parent", "task_advancements:tasks/root");
//                parameterChanges.put("title", "Task 120");
//                parameterChanges.put("show_toast", true);
//                //parameterChanges.put("announce_to_chat", "true");

                //_gameManager.InitializeTasks();
                //_gameManager.LogEnabledTasks();
                //_advancementManager.ModifyAdvancement("task1.json", "title", "task 69");
                //JsonManager _jsonManager = new JsonManager();
                //Map<Object,Object> outermostEntries = _jsonManager.JsonFileToMap(new File(_advancementManager.GetAdvancementPath() + "task1.json"));
                //_jsonManager.ModifyJsonFile(new File(_advancementManager.GetAdvancementPath() + "task1.json"), "title", "69");

//                String json = "{\"display\":{\"icon\":{\"id\":\"minecraft:map\"},\"title\":\"Task 120\",\"description\":\"...\",\"frame\":\"task\",\"show_toast\":true,\"announce_to_chat\":true,\"hidden\":false},\"parent\":\"task_advancements:tasks/root\",\"criteria\":{\"requirement\":{\"trigger\":\"minecraft:impossible\"}}}";
//                Gson gson = new Gson();
//                Map<Object,Object> map = gson.fromJson(json, Map.class);



//                HashMap<String, Object> parameterChanges = new HashMap<>();
//                parameterChanges.put("parent", "task_advancements:tasks/root");
//                parameterChanges.put("title", "Task 120");
//                parameterChanges.put("show_toast", true);
//                //parameterChanges.put("announce_to_chat", "true");
//
//                AdvancementManager _advancementManager = new AdvancementManager();
//                _advancementManager.ChangeAdvancementFile("task1.json", parameterChanges);
                return true;
            } else if (label.equalsIgnoreCase("t")) {
                //_chestManager.SetChestInventory(p);
                //Bukkit.broadcastMessage("changed chest inventory");
                T_Command(p, args);
            } else if (label.equalsIgnoreCase("spawn_villager")) {
                Spawn_Villager_Command(p, args);
            } else if (label.equalsIgnoreCase("set_spawn")) {
                Set_Spawn_Command(p, args);
            } else if (label.equalsIgnoreCase("get_custom_item")) {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("index")) {
                        int index = Integer.valueOf(args[1]);
                        if (_itemManager.GetCustomItem(index) != null) {
                            Bukkit.getWorld("void_world").dropItem(p.getLocation(), _itemManager.GetCustomItem(index));
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("name")) {
                        String arg = args[1];
                        Bukkit.getWorld("void_world").dropItem(p.getLocation(), _itemManager.GetCustomItem(_itemManager.ItemNameToIndex(arg)));
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else {
                    Bukkit.broadcastMessage("Format: get_custom_item index/name argument");
                }
                return false;
            }
            else if (label.equalsIgnoreCase("set_custom_item")) {
                String name = null;
                if (args.length == 2) {
                    int arg0 = Integer.valueOf(args[0]);
                    name = args[1];

                    _itemManager.SetCustomItem(p, arg0, name);
                    Bukkit.broadcastMessage("Added: " + _itemManager.GetCustomItem(arg0) + ", name = " + name.toUpperCase());
                    return true;
                }
                else {
                    Bukkit.broadcastMessage("Usage: set_custom_item index name(optional)");
                    return false;
                }
            }
            else if (label.equalsIgnoreCase("list_custom_items")) {
                int i = 0;
                for(CustomItems item : _itemManager.GetCustomItemsList()) {
                    Bukkit.broadcastMessage("index " + i + ": " + item.getItemName());
                    i++;
                }
            }
            else if (label.equalsIgnoreCase("add_custom_item")) {
                if (args.length == 1) {
                    String name = args[0];
                    _itemManager.AddCustomItem(p, name);
                    int index = _itemManager.GetCustomItemsList().size() - 1;
                    Bukkit.broadcastMessage("Added: " + _itemManager.GetCustomItem(index) + ", name = " + name.toUpperCase() + ", index = " + (index));
                    return true;
                }
                else {
                    Bukkit.broadcastMessage("Usage: add_custom_item name(optional)");
                    return false;
                }
            }
            else if (label.equalsIgnoreCase("set_starter_chest")) {
                _chestManager.SetChestInventory(p);
                return true;
            }

        }
        return false;
    }

    private boolean Spawn_Villager_Command(Player p, String [] args) {
        Location spawnLocation = p.getLocation(); // Get the player's location

        // Spawn a villager with all trades unlocked
        Villager villager = (Villager) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.VILLAGER);
        villager.setProfession(Villager.Profession.FARMER); // Set the villager's profession (optional)
//        villager.setAdult(); // Make the villager an adult
//        villager.setAgeLock(true); // Prevent the villager from aging
        //villager.setVillagerLevel(5); // Set the villager's level to master
        villager.setVillagerExperience(5000); // Set the villager's experience to the maximum
        return true;
    }

    private boolean Set_Spawn_Command(Player p, String[] args) {
        p.sendMessage(ChatColor.RED + "THIS COMMAND DOESN'T SET SPAWNS RIGHT NOW");

        if (args.length == 1) {
            String arg = args[0];
            if (arg.equalsIgnoreCase("blue")) {
                p.sendMessage(ChatColor.GOLD + "Blue spawn set");
                return true;

            } else if (arg.equalsIgnoreCase("red")) {
                p.sendMessage(ChatColor.GOLD + "Red spawn set");
                return true;
            } else {
                p.sendMessage(ChatColor.RED + "Usage: /set_spawn Blue/Red");
                return false;
            }
        }

        p.sendMessage(ChatColor.RED + "Usage: /t Blue/Red");
        return true;
    }
    private boolean T_Command(Player p, String[] args) {
        p.sendMessage(ChatColor.RED + "waaah");

        if (args.length == 1) {
            String arg = args[0];
            if(arg.equalsIgnoreCase("blue"))  {
                _gameManager.blueTeam.add(p);
                Bukkit.broadcastMessage("Blue Team: " + _gameManager.blueTeam);
                p.sendMessage(ChatColor.RED + "blue");
                return true;

            }
            else if(arg.equalsIgnoreCase("red"))  {
                _gameManager.redTeam.add(p);
                Bukkit.broadcastMessage("Red Team: " + _gameManager.redTeam);
                p.sendMessage(ChatColor.RED + "red");
                return true;
            }
            else {
                p.sendMessage(ChatColor.RED + "Usage: /t Blue/Red");
                return false;
            }
        }
        p.sendMessage(ChatColor.RED + "Usage: /t Blue/Red");
        return true;
    }
    private boolean IsPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Console can't use this command");
            return false;
        }
        return true;
    }

    private boolean IsCommandValid(CommandSender sender)
    {
        if (!IsPlayer(sender)) {
            return false;
        }
        else {
            return true;
        }
    }
}
