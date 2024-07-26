package org.jacobn99.skyblockgambit;

import com.google.gson.Gson;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.C;
import org.jacobn99.skyblockgambit.CustomAdvancements.AdvancementManager;
import org.jacobn99.skyblockgambit.CustomAdvancements.CraftX;
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
    public Set<Player> participatingPlayers;
    public HashMap<Long, Process> processes;
    public Map<Player, Integer> killCounts;
    JavaPlugin _mainPlugin;
    private ArmorStand blueArmorStand;
    private ArmorStand redArmorStand;
    private PortalManager _portalManager;
    public AdvancementManager advancementManager;
    private ProcessManager _processManager;
    private WorldManager _worldManager;
    private StarterChestManager _chestManager;
    private CustomVillagerManager _customVillagerManager;
    private CustomItemManager _customItemManager;
    private Team blueTeam;
    private Team redTeam;
    public CraftX craftX;
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
        participatingPlayers = new HashSet<>();

        processes = new HashMap<>();
        killCounts = new HashMap<>();
        _portalManager = new PortalManager(this);
        advancementManager = new AdvancementManager(_mainPlugin, teams);
        _processManager = new ProcessManager();
        _customVillagerManager = new CustomVillagerManager(_mainPlugin, customVillagers, this);
        _worldManager = new WorldManager(_mainPlugin, this, _portalManager, _processManager, _customVillagerManager);
        _chestManager = new StarterChestManager(_mainPlugin);
        _customItemManager = new CustomItemManager(_mainPlugin);
        blueTeam = new Team("blue", this);
        redTeam = new Team("red", this);
        craftX = new CraftX(advancementManager, _customItemManager, _mainPlugin);
        tickRate = 3;
        minWorldHeight = 94;
        normalVillagerAmount = 2;
        canProceed = true;
        isWorldGenerated = false;
    }
    public void Start() {

        Bukkit.broadcastMessage("Starting...");

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
        //If statement checks if defaultConfiguration.json (which is used load advancement files that don't exit yet)

        //if(Files.exists(Paths.get(advancementManager.GetAdvancementPath() + "default_configuration.json")) && Files.exists(Paths.get(advancementManager.GetAdvancementPath() + "enderdragon.json"))) {
        if(Files.exists(Paths.get(advancementManager.GetAdvancementPath() + "default_configuration.json"))) {
            if(!advancementManager.customAdvancements.isEmpty()) {
                advancementManager.customAdvancements.clear();
            }

            //CustomAdvancement twoKills = new CustomAdvancement("twoKills", new ItemStack(Material.DIAMOND), advancementManager.customAdvancements);
            //CustomAdvancement sabotage = new CustomAdvancement("sabotage", new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement reach_level_X = new CustomAdvancement("reach_level", new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement kill_two_players = new CustomAdvancement("kill_two_players", new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement kill_enderdragon = new CustomAdvancement("kill_enderdragon", new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement craft_item = new CustomAdvancement("craft_item", new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement get_glowing = new CustomAdvancement("get_glowing", new ItemStack(Material.DIAMOND), advancementManager);


            for (CustomAdvancement a : advancementManager.GetCustomAdvancementList()) {
                a.LoadFile(advancementManager.GetDefaultConfiguration());
            }
            //advancementManager.RandomizeTasks();
            }
        else {
            Bukkit.broadcastMessage("ERROR: Missing mandatory files in tasks folder (enderdragon.json and/or default_configuration.json");
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
        InitializeTasks();
        advancementManager.ClearTaskParents();
        advancementManager.RandomizeTasks();
        craftX.WriteToCraftXFile();
        craftX.UpdateDescription();
//        advancementManager.ModifyAdvancement(new File(advancementManager.GetAdvancementPath() + "/craft_item.json"), "description", "brooo");


        Reset();
    }
    public void LogEnabledTasks() {
        File file = new File(_mainPlugin.getDataFolder().getAbsolutePath() + "/tasks_list.json");

        try {
            Writer writer = Files.newBufferedWriter(file.toPath());
            Gson gson = new Gson();
            gson.toJson(advancementManager.futureEnabledAdvancementNames, writer);

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
    public void GenerateEndPortal(Location loc) {
        BlockFace face;
        loc.subtract(1, 0, 2);
        face = BlockFace.SOUTH;
        for(int e = 0; e < 2; e++) {
            for (int i = 0; i < 3; i++) {
                Location currentLoc = loc.clone();
                currentLoc.setX(loc.getX() + i);
                currentLoc.setZ(loc.getZ() + (e*4));
                currentLoc.getBlock().setType(Material.END_PORTAL_FRAME);
                Directional directionalBlock = (Directional) currentLoc.getBlock().getBlockData();
                directionalBlock.setFacing(face);
                currentLoc.getBlock().setBlockData(directionalBlock);

                currentLoc = null;

            }
            face = BlockFace.NORTH;
        }
        face = BlockFace.EAST;
        for(int e = 0; e < 2; e++) {
            for (int i = 0; i < 3; i++) {
                Location currentLoc = loc.clone();
                currentLoc.setZ(loc.getZ() + 1 + i);
                currentLoc.setX(loc.getX() + (e*4) - 1);
                currentLoc.getBlock().setType(Material.END_PORTAL_FRAME);

                Directional directionalBlock = (Directional) currentLoc.getBlock().getBlockData();
                directionalBlock.setFacing(face);
                currentLoc.getBlock().setBlockData(directionalBlock);
                currentLoc = null;

            }
            face = BlockFace.WEST;
        }
    }

}
