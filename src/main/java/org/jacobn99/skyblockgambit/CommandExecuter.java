package org.jacobn99.skyblockgambit;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomAdvancements.AdvancementManager;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.CustomItems.CustomItems;
import org.jacobn99.skyblockgambit.CustomWorlds.WorldCopier;
import org.jacobn99.skyblockgambit.CustomWorlds.WorldManager;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.StarterChest.StarterChestManager;

import javax.swing.filechooser.FileSystemView;
import java.util.List;

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
    AnimalSpawner _animalSpanwer;
    private DataManager _DataManager;
    //ProcessManager _processManager;
    public CommandExecuter(JavaPlugin mainPlugin, GameManager gameManager) {
        _mainPlugin = mainPlugin;
        _gameManager = gameManager;
        _itemManager = _gameManager._customItemManager;
        //_portalManager = new PortalManager();
        _chestManager = new StarterChestManager(_mainPlugin);
        _advancementManager = _gameManager.advancementManager;
        _portalManager = new PortalManager(_gameManager, _gameManager._processManager);
        _worldManager = _gameManager._worldManager;
        _worldCopier = _worldManager._worldCopier;
        _animalSpanwer = new AnimalSpawner(_gameManager, _worldManager, _gameManager._processManager);
        _DataManager = new DataManager();

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


                //_gameManager.blueTeam.add(p);
//                _gameManager.redTeam.clear();
                //Bukkit.broadcastMessage("red team: " + _gameManager.GetRedTeamList());
                return true;
            } else if (label.equalsIgnoreCase("debug")) {
                sender.sendMessage(ChatColor.RED + "debug");
                //Team team = _gameManager.FindPlayerTeam(p);
                Inventory tasksInventory = Bukkit.createInventory(null, 9, "Tasks");

                _advancementManager.InitializeTaskInventory(tasksInventory);
                //team.killsInventory.setContents(_advancementManager.CreateTaskInventory(p).getContents());
                //team.killsInventory.setItem(0, new ItemStack(Material.DIAMOND));

                p.openInventory(tasksInventory);
//                int slot = _gameManager.FindInInventory(p.getInventory(), _itemManager.GetCustomItem(_itemManager.ItemNameToIndex("PORTAL_OPENER")));
//                Bukkit.broadcastMessage("Slot: " + slot);
                //_advancementManager.RandomizeTasks();
//                NetherManager netherManager = new NetherManager(_gameManager, _gameManager._processManager, _worldManager);
//                netherManager.ClearSpawnPortalArea(p.getLocation());
                //netherManager.GenerateNetherPortal(p.getLocation().add(3, 0, 0));
//                World nether = Bukkit.getWorld("void_world_nether");
//                Location loc = _gameManager.FindSurface(p.getLocation(), 100, 30);
//                if(loc != null) {
//                    p.teleport(loc);
//                }
//                Bukkit.broadcastMessage("a nether fortress loc: " + Bukkit)
//                Bukkit.broadcastMessage("loc again: " + nether.locateNearestStructure(new Location(nether, 0, 0 ,0), StructureType.NETHER_FORTRESS, 5000, false));
//                Bukkit.broadcastMessage("loc: " + p.getWorld().locateNearestStructure(p.getLocation(), StructureType.NETHER_FORTRESS, 5000, false));
//                String file = ;
//                Bukkit.broadcastMessage("update folder: " + file);
                //CustomWorld redWorld = new CustomWorld(_worldManager, new Location(Bukkit.getWorld("void_world"), 21,  100, 62), _gameManager.customWorlds);
                //Bukkit.broadcastMessage("advancement file path??: " + _advancementManager.advancementsPath);
//                Bukkit.broadcastMessage("world file path: " + Bukkit.getServer().getWorldContainer().getAbsolutePath());
//                Bukkit.broadcastMessage("HomeDirectory: " + FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath().replace('\\', '/'));
//                Bukkit.broadcastMessage("data file path: " + _mainPlugin.getDataFolder().getAbsolutePath());
                //_worldCopier.ClearWorld(_gameManager.FindPlayerTeam(p).GetTeamWorld().GetMiddleLoc(), _worldManager.get_worldLength());
                //_worldCopier.ClearWorld(_gameManager.FindPlayerTeam(p).GetTeamWorld().GetMiddleLoc(), _worldManager.get_worldLength());
                //p.openInventory(_gameManager.FindPlayerTeam(p).killsInventory);

//                File file = new File( _mainPlugin.getDataFolder().getAbsolutePath() + "/gamer.json");
//
//                List<Object> objects = new ArrayList<>();
//                objects.add(new ItemStack(Material.CAKE));
//                _DataManager.WriteToFile(file, objects, new ItemStackSerialization());
//                objects = _DataManager.GetObjects(file, new ItemStackSerialization());
//                Bukkit.broadcastMessage(objects.get(0).toString());
                //_gameManager.GenerateInvaderPortalFrame(p.getLocation());
                //_gameManager.GrantTeamCompasses();
                //_worldManager.SpawnTeamVillagers(_gameManager.getCustomVillagers(), _gameManager._customVillagerManager);
//                Team team = _gameManager.FindPlayerTeam(p);
//                _animalSpanwer.SpawnAnimals();
//                Bukkit.broadcastMessage("animal population: " + _animalSpanwer.GetAnimalPopulation(team.GetTeamWorld()));

//                _gameManager.InitializeTasks();
//                _gameManager.advancementManager.ClearTaskParents();
                //_gameManager.advancementManager.RandomizeTasks();
                //_gameManager.advancementManager.RandomizeTasks();
                //Bukkit.broadcastMessage("current tasks: " + _gameManager.advancementManager.GetCurrentEnabledTaskNames());
                //_gameManager.GenerateEndPortal(p.getLocation());

//                _gameManager.craftX.WriteToCraftXFile();
//                _gameManager.craftX.UpdateDescription();

                //_gameManager.InitializeTasks();
//                File file = new File( _mainPlugin.getDataFolder().getAbsolutePath() + "/output.json");
//                _worldManager.BuildWorld(_gameManager.redWorld, file, _processManager);
                //_worldCopier.DuplicateLand(p.getLocation(), file);

                //_worldCopier.PasteChunkPiece(_worldCopier.GetChunkPieceData(file.getAbsolutePath()), 0, p.getLocation());
//
                //_worldCopier.DuplicateLand(p.getLocation(), file);
                //Bukkit.broadcast  Message("latest execution time: " + _processManager.GetLatestExecutionTime(_gameManager.processes));

//                for(Portal portal : _gameManager.portals) {
//                    Bukkit.broadcastMessage("Portal Loc: " + portal.GetPortalLocation());
//                }
//                for(CustomWorld customWorld : _gameManager.customWorlds) {
//                    Bukkit.broadcastMessage("Reference Corner: " + customWorld.GetReferenceCorner());
//                }
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
            else if(label.equalsIgnoreCase("clear_custom_items")) {
                _itemManager.ClearFile();
                Bukkit.broadcastMessage("Items cleared");
            }
            else if (label.equalsIgnoreCase("set_starter_chest")) {
                _chestManager.SetChestInventory(p);
                return true;
            }
            else if (label.equalsIgnoreCase("grant_advancement")) {
                if (args.length == 1) {
                    if(_gameManager.advancementManager.GetAdvancement(args[0]) != null) {
                        Bukkit.broadcastMessage("Granting advancement");
                        _gameManager.advancementManager.GrantTeamAdvancement(p,
                                _gameManager.advancementManager.GetAdvancement(args[0]), false);
                    }
                    else {
                        Bukkit.broadcastMessage("Not a valid advancement");
                    }
                    return true;
                }
                else {
                    Bukkit.broadcastMessage("Usage: grant_advancement (advancement name)");
                    return false;
                }
            }
            else if (label.equalsIgnoreCase("clear_world")) {
                if (args.length == 1) {
                    String worldName = args[0];
                    for(Team team : _gameManager.teams) {
                        if(team.GetTeamColor().equalsIgnoreCase(worldName)) {
                            //Bukkit.broadcastMessage(team.GetTeamColor() + ": " + team.GetTeamWorld().GetMiddleLoc());
                            _worldCopier.ClearWorld(team.GetTeamWorld().GetMiddleLoc(), _worldManager.get_worldLength());
                        }
                    }
                    return true;
                }
                else {
                    Bukkit.broadcastMessage("Usage: clear_world (team name)");
                    return false;
                }
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
            List<Team> _teams = _gameManager.teams;

            for(Team team : _teams) {
                if(team.GetTeamColor().equalsIgnoreCase(arg)) {
                    team.AddMember(p);
                    Bukkit.broadcastMessage(team.GetTeamColor() + " Team: " + team.GetMembers());
                    return true;
                }
            }
            p.sendMessage(ChatColor.RED + "Usage: /t Blue/Red");
            return false;
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
