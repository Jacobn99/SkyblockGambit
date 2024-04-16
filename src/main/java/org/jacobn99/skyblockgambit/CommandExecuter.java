package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandExecuter implements CommandExecutor {
    private JavaPlugin _mainPlugin;
    private GameManager _gameManager;
    private PortalManager _portalManager;
    private ConfigManager _configManager;
    private CustomVillager _customVillager;
    public CommandExecuter(JavaPlugin mainPlugin) {
        _mainPlugin = mainPlugin;
        _gameManager = new GameManager(_mainPlugin);
        _configManager = new ConfigManager(_mainPlugin);
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (IsCommandValid(sender)) {
            Player p = (Player)sender;

            if (label.equalsIgnoreCase("start")) {
                sender.sendMessage(ChatColor.RED + "start");
                _gameManager.Start();
                return true;
            }
            else if (label.equalsIgnoreCase("debug")) {
                sender.sendMessage(ChatColor.RED + "debug");


//                for(String s : _configManager.GetArguments("Villager1", "Trades", "Trade1")) {
//                    Bukkit.broadcastMessage(s);
//                }
//                for(String s : _configManager.GetArguments("Villager1")) {
//                    Bukkit.broadcastMessage(s);
//                }

//                for(String s : _mainPlugin.getConfig().getStringList("Villager1")) {
//                    Bukkit.broadcastMessage(s);
//                }

//                for(Portal portal : _gameManager.portals) {
//                    //Bukkit.broadcastMessage("portal loc: " + portal.GetPortalLocation());
//                    portal.Activate();
//                }
//                Portal _portal = new Portal(new Location(Bukkit.getWorld("void_world"), 112, -60, 0));
//                _portal.ActivatePortal();
                //_portal.TeleportIsland(p, _gameManager.GetRedSpawn());
                //_portalManager.TeleportIsland(p, new Location(Bukkit.getWorld("void_world"), 112, -60, 4));
//                Bukkit.getWorld("void_world").getBlockAt(113, -60, 160).setType(Material.CHEST);
//                Location loc = new Location(Bukkit.getWorld("void_world"), 113, -60, 160);
//                Chest crate = (Chest) Bukkit.getWorld("void_world").getBlockAt(loc).getState();
//                _gameManager.CheckChest(crate);
                return true;
            }
            else if (label.equalsIgnoreCase("t")) {
                T_Command(p, args);
            }
            else if (label.equalsIgnoreCase("spawn_villager")) {
                Spawn_Villager_Command(p, args);
            }
            else if (label.equalsIgnoreCase("set_spawn")) {
                Set_Spawn_Command(p, args);
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
        p.sendMessage(ChatColor.RED + "Skibidi");

        if (args.length == 1) {
            String arg = args[0];
            if (arg.equalsIgnoreCase("blue")) {
                //_gameManager.blueSpawn = p.getLocation();
                p.sendMessage(ChatColor.GOLD + "Blue spawn set");
                return true;

            } else if (arg.equalsIgnoreCase("red")) {
                //_gameManager.redSpawn = p.getLocation();
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
                //p.setRespawnLocation(_gameManager.blueSpawn, true);
                p.sendMessage(ChatColor.RED + "blue");
                return true;

            }
            else if(arg.equalsIgnoreCase("red"))  {
                _gameManager.redTeam.add(p);
                //p.setRespawnLocation(_gameManager.redSpawn, true);
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
