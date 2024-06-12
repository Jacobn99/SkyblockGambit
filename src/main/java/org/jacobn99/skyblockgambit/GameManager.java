package org.jacobn99.skyblockgambit;

import com.google.gson.Gson;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jacobn99.skyblockgambit.CustomAdvancements.AdvancementManager;
import org.jacobn99.skyblockgambit.CustomAdvancements.CustomAdvancement;
import org.jacobn99.skyblockgambit.GeneratorInfo.DiamondGenerator;
import org.jacobn99.skyblockgambit.GeneratorInfo.IronGenerator;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GameManager {
    private Location blueSpawn;
    private Location redSpawn;
    long tickRate;
    boolean isRunning;
    private ArrayList<Generator> generatorList;
    private ArrayList<Entity> disposableEntities;
    private List<Portal> portals;
    private List<CustomVillager> customVillagers;
    private List<StarterChest> starterChestList;
    private List<Object> objects;
    //private List<CustomAdvancement> customAdvancements;
    private Set<Set<Player>> teams;
    public Set<Player> blueTeam;
    public Set<Player> redTeam;
    Portal redPortal;
    Portal bluePortal;
    HashMap<Long, Queueable> processes;
    JavaPlugin _mainPlugin;
    private ArmorStand blueArmorStand;
    private ArmorStand redArmorStand;
    private PortalManager _portalManager;
    private AdvancementManager _advancementManager;

    public GameManager(JavaPlugin mainPlugin) {
        blueTeam = new HashSet();
        redTeam = new HashSet();
        teams = new HashSet<>();

        disposableEntities = new ArrayList<>();
        generatorList = new ArrayList();
        portals = new ArrayList<>();
        starterChestList = new ArrayList<>();
        customVillagers = new ArrayList();
        objects = new ArrayList<>();

        processes = new HashMap<>();
        _portalManager = new PortalManager();
        _advancementManager = new AdvancementManager(_mainPlugin);

        teams.add(blueTeam);
        teams.add(redTeam);
        _mainPlugin = mainPlugin;
        tickRate = 3;
        Bukkit.getConsoleSender().sendMessage("new gamemanager");
        //Bukkit.broadcastMessage("new gameManager");
    }
    public void Start() {
        isRunning = true;

        StarterChestManager _chestManager = new StarterChestManager(_mainPlugin);
        World world = Bukkit.getWorld("void_world");
        //StarterChest chest1 = new StarterChest()

        Generator blueGen1 = new DiamondGenerator(generatorList, new Location(world,124, -60, 163),
                new Location(world,122, -61, 163));
        Generator blueGen2 = new IronGenerator(generatorList, new Location(world,102, -60, 163),
                new Location(world,104, -61, 163));
        Generator redGen1 = new IronGenerator(generatorList, new Location(world,101, -60, 4),
                new Location(world,103, -61, 4));
        Generator redGen2 = new DiamondGenerator(generatorList, new Location(world,123, -60, 4),
                new Location(world,121, -61, 4));

        bluePortal = new Portal(portals, _portalManager, GetRedSpawn(),
                new Location(Bukkit.getWorld("void_world"), 113, -60, 168));
        redPortal = new Portal(portals, _portalManager, GetBlueSpawn(),
                new Location(Bukkit.getWorld("void_world"), 112, -60, 0));

        StarterChest blueChest = new StarterChest(new Location(world, 113, -60, 160), _chestManager.GetInventory(), starterChestList);
        blueChest.CreateChest();

        StarterChest redChest = new StarterChest(new Location(world, 112, -60, 7), _chestManager.GetInventory(), starterChestList);
        redChest.CreateChest();


        UpdateSpawns();
        SpawnTeamVillagers();
        //InitializeTaskObjects();

        Borderwall _borderwall = new Borderwall(_mainPlugin, this);
        _borderwall.createBorder(GetBlueSpawn(), GetRedSpawn());

        new BukkitRunnable() {
            @Override
            public void run() {
                //UpdateSpawns();
                if(isRunning != true) {
                    this.cancel();
                }
                HandleProcesses();
                RenewGenerators(tickRate);
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
    public void EndGame() {
        isRunning = false;
        Bukkit.broadcastMessage("size: " + disposableEntities.size());

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
    private void HandleProcesses() {
        World world = Bukkit.getWorld("void_world");
        for(Long executionTime : processes.keySet()) {
            Bukkit.broadcastMessage(world.getFullTime() + ", " + executionTime);
            if(world.getFullTime() >= executionTime) {
                processes.get(executionTime).Execute();
                processes.remove(executionTime);
            }
        }
    }
    public void UpdateSpawns() {
        if(blueArmorStand == null || redArmorStand == null) {
            for (Entity e : Bukkit.getWorld("void_world").getEntities()) {
                //Bukkit.broadcastMessage("scoreboard: " + e.getScoreboardTags() + ", loc: " + e.getLocation());
                if (e.getScoreboardTags().contains("BlueSpawn")) {
                    //Bukkit.broadcastMessage("gamer blue alert");
                    blueSpawn = e.getLocation();
                    blueArmorStand = (ArmorStand) e;
                }
                else if (e.getScoreboardTags().contains("RedSpawn")) {
                    //Bukkit.broadcastMessage("gamer red alert");
                    redSpawn = e.getLocation();
                    redArmorStand = (ArmorStand) e;
                }
            }
        }
        for (Player p : redTeam) {
            p.setRespawnLocation(redSpawn, true);
        }
        for (Player p : blueTeam) {
            p.setRespawnLocation(blueSpawn, true);
        }
    }

    public Set<Player> GetBlueTeamList() {
        return blueTeam;
    }
    public Set<Player> GetRedTeamList() {
        return redTeam;
    }
    public Location GetBlueSpawn() {
        UpdateSpawns();
        return blueSpawn.clone();
    }
    public Location GetRedSpawn() {
        UpdateSpawns();
        return redSpawn.clone();
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

    public void SpawnTeamVillagers() {
        Location spawnLoc = GetBlueSpawn();

        for(int i = 0; i < 2; i++) {
            SpawnVillager(spawnLoc, Villager.Profession.CLERIC);
            SpawnVillager(spawnLoc, Villager.Profession.FARMER);
            SpawnVillager(spawnLoc, Villager.Profession.ARMORER);
            SpawnVillager(spawnLoc, Villager.Profession.BUTCHER);
            SpawnVillager(spawnLoc, Villager.Profession.CARTOGRAPHER);
            SpawnVillager(spawnLoc, Villager.Profession.FISHERMAN);
            SpawnVillager(spawnLoc, Villager.Profession.FLETCHER);
            SpawnVillager(spawnLoc, Villager.Profession.LIBRARIAN);
            SpawnVillager(spawnLoc, Villager.Profession.MASON);
            SpawnVillager(spawnLoc, Villager.Profession.SHEPHERD);
            SpawnVillager(spawnLoc, Villager.Profession.TOOLSMITH);
            SpawnVillager(spawnLoc, Villager.Profession.WEAPONSMITH);

            CreateCustomVillager("Villager0", spawnLoc, Villager.Profession.NITWIT);
            CreateCustomVillager("Villager1", spawnLoc, Villager.Profession.NITWIT);

            spawnLoc = GetRedSpawn();
        }
    }

    private Villager SpawnVillager(Location loc, Villager.Profession profession) {
        // Spawn a villager with all trades unlocked
        Villager villager = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        villager.setProfession(profession); // Set the villager's profession (optional)
        villager.setVillagerExperience(5000); // Set the villager's experience to the maximum
        disposableEntities.add(villager);
        return villager;
    }

    private CustomVillager CreateCustomVillager(String preset, Location loc, Villager.Profession profession) {
        CustomVillager custom = new CustomVillager(_mainPlugin,
                SpawnVillager(loc, profession));
        custom.SetTrades(preset);
        custom.getVillager().setVillagerLevel(5);
        customVillagers.add(custom);
        return custom;
    }
    public void Reset() {
        objects.addAll(portals);
        objects.addAll(generatorList);
        objects.addAll(customVillagers);
        objects.addAll(disposableEntities);
        objects.addAll(starterChestList);
        objects.addAll(teams);

        portals.clear();
        teams.clear();
        disposableEntities.clear();
        customVillagers.clear();
        generatorList.clear();
        starterChestList.clear();
        processes = null;

        for(Object o : objects) {
            o = null;
        }
        System.gc();
    }
}
