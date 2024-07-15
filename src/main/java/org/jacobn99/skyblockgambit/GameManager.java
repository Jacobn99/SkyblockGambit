package org.jacobn99.skyblockgambit;

import com.google.gson.Gson;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.C;
import org.jacobn99.skyblockgambit.CustomAdvancements.AdvancementManager;
import org.jacobn99.skyblockgambit.CustomAdvancements.CustomAdvancement;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.CustomWorlds.CustomWorld;
import org.jacobn99.skyblockgambit.CustomWorlds.WorldManager;
import org.jacobn99.skyblockgambit.GeneratorInfo.DiamondGenerator;
import org.jacobn99.skyblockgambit.GeneratorInfo.Generator;
import org.jacobn99.skyblockgambit.GeneratorInfo.IronGenerator;
import org.jacobn99.skyblockgambit.Portals.Portal;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.Processes.Process;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;
import org.jacobn99.skyblockgambit.StarterChest.StarterChest;
import org.jacobn99.skyblockgambit.StarterChest.StarterChestManager;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GameManager {
//    private Location blueSpawn;
//    private Location redSpawn;
    long tickRate;
    public boolean isRunning;
    private ArrayList<Generator> generatorList;
    public ArrayList<Entity> disposableEntities;
    public List<Portal> portals;
    public List<CustomWorld> customWorlds;
    private List<CustomVillager> customVillagers;

    private List<StarterChest> starterChestList;
    private List<Object> objects;
    public List<Team> teams;
    public HashMap<Long, Process> processes;

    JavaPlugin _mainPlugin;
    private ArmorStand blueArmorStand;
    private ArmorStand redArmorStand;
    private PortalManager _portalManager;
    private AdvancementManager _advancementManager;
    private ProcessManager _processManager;
    private WorldManager _worldManager;
    private StarterChestManager _chestManager;
    private CustomVillagerManager _customVillagerManager;
    private CustomItemManager _customItemManager;
    private Team blueTeam;
    private Team redTeam;
    CustomWorld blueWorld;
    CustomWorld redWorld;
    //public List<ProcessGroup> processGroups;
    public boolean canProceed;
    public boolean isWorldGenerated;
    public int minWorldHeight;
    public int normalVillagerAmount;


    public GameManager(JavaPlugin mainPlugin) {
        _mainPlugin = mainPlugin;

        disposableEntities = new ArrayList<>();
        generatorList = new ArrayList();
        portals = new ArrayList<>();
        starterChestList = new ArrayList<>();
        customVillagers = new ArrayList();
        customWorlds = new ArrayList<>();
        objects = new ArrayList<>();
        teams = new ArrayList<>();

        processes = new HashMap<>();
        _portalManager = new PortalManager(this);
        _advancementManager = new AdvancementManager(_mainPlugin);
        _processManager = new ProcessManager();
        _customVillagerManager = new CustomVillagerManager(_mainPlugin, customVillagers, this);
        _worldManager = new WorldManager(_mainPlugin, this, _portalManager, _processManager, _customVillagerManager);
        _chestManager = new StarterChestManager(_mainPlugin);
        _customItemManager = new CustomItemManager(_mainPlugin);
        blueTeam = new Team(null, "blue", teams);
        redTeam = new Team(null, "red", teams);
        tickRate = 3;
        minWorldHeight = 94;
        normalVillagerAmount = 2;
        canProceed = true;
        isWorldGenerated = false;
    }
    public void Start() {
        isRunning = true;
        World world = Bukkit.getWorld("void_world");
        File file = new File( _mainPlugin.getDataFolder().getAbsolutePath() + "/output.json");

        _customItemManager.LoadRequiredItems();
        InitializeTasks();

        blueWorld = new CustomWorld(_worldManager, new Location(world, -160, 100, -136), customWorlds);
        redWorld = new CustomWorld(_worldManager, new Location(world, 21,  100, 62), customWorlds);

        _worldManager.BuildWorld(redWorld, file, _processManager);
        _worldManager.BuildWorld(blueWorld, file, _processManager);

        _processManager.CreateProcess(processes, _processManager.GetLatestExecutionTime(processes) + 50,
                () -> _worldManager.AddPostGenerationObjects(_chestManager, _customVillagerManager, customVillagers));

        InitializeTeams();
        UpdateSpawns();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!isRunning) {
                    this.cancel();
                }
                _processManager.HandleProcesses(processes);
                //RenewGenerators(tickRate);
                _portalManager.PortalUpdate(portals);
            }
        }.runTaskTimer(_mainPlugin, 0, tickRate);
    }

    public void InitializeTasks() {
        if(Files.exists(Paths.get(_advancementManager.GetAdvancementPath() + "defaultConfiguration.json")) && Files.exists(Paths.get(_advancementManager.GetAdvancementPath() + "enderdragon.json"))) {
            CustomAdvancement sabotage = new CustomAdvancement("sabotage", new ItemStack(Material.DIAMOND), _advancementManager.customAdvancements);
            CustomAdvancement reach_level_32 = new CustomAdvancement("reach_level_32", new ItemStack(Material.DIAMOND),  _advancementManager.customAdvancements);
            CustomAdvancement task3 = new CustomAdvancement("kill_two_players", new ItemStack(Material.DIAMOND), _advancementManager.customAdvancements);
            CustomAdvancement task4 = new CustomAdvancement("task4", new ItemStack(Material.DIAMOND), _advancementManager.customAdvancements);

            for (CustomAdvancement a : _advancementManager.GetCustomAdvancementList()) {
                a.LoadFile(_advancementManager.GetDefaultConfiguration());
            }
            _advancementManager.RandomizeTasks();}
        else {
            Bukkit.broadcastMessage("ERROR: Missing mandatory files in tasks folder (enderdragon.json and/or defaultConfiguration.json");
        }

    }
    public void InitializeTeams() {
        AssignTeamWorlds();
    }
    public void AssignTeamWorlds() {
        int i = 0;
        Team team;
        for(CustomWorld customWorld : customWorlds) {
            team = teams.get(i);
            team.SetTeamWorld(customWorld);
            i++;
        }
    }
//    private void InitializeObjects() {
//        StarterChestManager _chestManager = new StarterChestManager(_mainPlugin);
//        World world = Bukkit.getWorld("void_world");
//        //StarterChest chest1 = new StarterChest()
//
//        Generator blueGen1 = new DiamondGenerator(generatorList, new Location(world,124, -60, 163),
//                new Location(world,122, -61, 163));
//        Generator blueGen2 = new IronGenerator(generatorList, new Location(world,102, -60, 163),
//                new Location(world,104, -61, 163));
//        Generator redGen1 = new IronGenerator(generatorList, new Location(world,101, -60, 4),
//                new Location(world,103, -61, 4));
//        Generator redGen2 = new DiamondGenerator(generatorList, new Location(world,123, -60, 4),
//                new Location(world,121, -61, 4));
//    }
    public Location FindSurface(Location loc, double maxY, double minY) {
        Location scan = loc;
        for(double i = maxY; i > minY; i--) {
            scan.setY(i);

            if(scan.getBlock().getType() != Material.AIR) {
                if(Bukkit.getWorld("void_world").getBlockAt((int)scan.getX(), (int)scan.getY() + 1, (int)scan.getZ()).getType() == Material.AIR &&
                        Bukkit.getWorld("void_world").getBlockAt((int)scan.getX(), (int)scan.getY() + 2, (int)scan.getZ()).getType() == Material.AIR) {
                    scan.setY(scan.getY() + 1);
                    return scan;
                }
            }
        }
        return null;
    }
    public void EndGame() {
        isRunning = false;
        for(Entity e : disposableEntities) {
            e.remove();
        }

        Reset();
    }
    public void LogEnabledTasks() {
        File file = new File(_mainPlugin.getDataFolder().getAbsolutePath() + "/tasks_list.json");

        try {
            Writer writer = Files.newBufferedWriter(file.toPath());
            Gson gson = new Gson();
            gson.toJson(_advancementManager.enabledAdvancementNames, writer);

            //writer.write("fortnite");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public void UpdateSpawns() {
        //if()
//    blueSpawn = blueWorld.GetWorldSpawn();
//    redSpawn =  redWorld.GetWorldSpawn();

    if(isWorldGenerated) {
        Bukkit.broadcastMessage("worlds have generated");
        for (Team t : teams) {
            for (Player p : t.GetMembers()) {
                p.setRespawnLocation(t.GetTeamWorld().GetWorldSpawn(this), true);
            }
        }
    }
}
    public List<StarterChest> GetStarterChestList() {
        return starterChestList;
    }

    private void RenewGenerators(long tickRate) {
//        ItemStack fuel = new ItemStack(Material.STONE, 1);
        for(Generator g : generatorList) {
            if(g.GetLocation() != null && g.IsFuelAvailable()) {
                if (g.GetGenerateTimeRemaining() <= 0) {
                    g.Generate();
                    //g.GetFuelChest().getBlockInventory().removeItem(g.GetFuel());
                    g.AddGenerateTime(g.GetGenerateDelay() + tickRate);
                    //break;
                }
                if (g.GetFuelTimeRemaining() <= 0) {
                    g.GetFuelChest().getBlockInventory().removeItem(g.GetFuel());
                    g.AddFuelTime(g.GetFuelDelay() + tickRate);
                    //break;
                }
                //Bukkit.broadcastMessage("Time Remaining: " + g.GetTimeRemaining());
                g.AddGenerateTime(-tickRate);
                g.AddFuelTime(-tickRate);
                //g.AddTime();
            }
        }
    }


    public void Reset() {
        for(Portal p : portals) {
            p.Deactivate();
        }
        for(StarterChest chest : starterChestList) {
            chest.DestroyChest();
        }

        objects.addAll(portals);
        objects.addAll(generatorList);
        objects.addAll(customVillagers);
        objects.addAll(disposableEntities);
        objects.addAll(starterChestList);
        //objects.addAll(teams);
        objects.addAll(customWorlds);

        customWorlds.clear();
        portals.clear();
        //teams.clear();
        disposableEntities.clear();
        customVillagers.clear();
        generatorList.clear();
        starterChestList.clear();
        processes.clear();

        for(Object o : objects) {
            o = null;
        }
        System.gc();
    }
    public List<CustomVillager> getCustomVillagers() {
        return customVillagers;
    }
    public Team FindPlayerTeam(Player p) {
        for (Team t : teams) {
            if (t.GetMembers().contains(p)) {
                return t;
            }
        }
        return null;
    }
}
