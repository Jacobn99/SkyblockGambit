package org.jacobn99.skyblockgambit;

import com.google.gson.Gson;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jacobn99.skyblockgambit.CustomAdvancements.*;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.CustomVillagers.CustomVillager;
import org.jacobn99.skyblockgambit.CustomVillagers.CustomVillagerManager;
import org.jacobn99.skyblockgambit.CustomWorlds.CustomWorld;
import org.jacobn99.skyblockgambit.CustomWorlds.WorldManager;
import org.jacobn99.skyblockgambit.GeneratorInfo.GeneratorManager;
import org.jacobn99.skyblockgambit.GeneratorInfo.ItemGenerator;
import org.jacobn99.skyblockgambit.Portals.Portal;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.Processes.Process;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
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
    //public ArrayList<Generator> generatorList;
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
//    private ArmorStand blueArmorStand;
//    private ArmorStand redArmorStand;
    private PortalManager _portalManager;
    public AdvancementManager advancementManager;
    public ProcessManager _processManager;
    public WorldManager _worldManager;
    private StarterChestManager _chestManager;
    public CustomVillagerManager _customVillagerManager;
    public CustomItemManager _customItemManager;
    public AnimalSpawner _animalSpawner;
    public GeneratorManager _generatorManager;
    public NetherManager netherManager;
    public XStacks xStacks;
    private Team blueTeam;
    private Team redTeam;
    public CraftX craftX;
    public GetGlowing getGlowing;
    CustomWorld blueWorld;
    CustomWorld redWorld;
    //public List<ProcessGroup> processGroups;
    public boolean canProceed;
    public boolean isWorldGenerated;
    public int minWorldHeight;
    public int normalVillagerAmount;
    private int _passiveMobCap;
    World overworld;
    public List<Inventory> nonClickableInventories;


    public GameManager(JavaPlugin mainPlugin) {
        _mainPlugin = mainPlugin;
        disposableEntities = new ArrayList<>();
        //generatorList = new ArrayList();
        portals = new ArrayList<>();
        starterChestList = new ArrayList<>();
        customVillagers = new ArrayList();
        customWorlds = new ArrayList<>();
        objects = new ArrayList<>();
        teams = new ArrayList<>();
        nonClickableInventories = new ArrayList<>();

        participatingPlayers = new HashSet<>();
        processes = new HashMap<>();
        killCounts = new HashMap<>();
        advancementManager = new AdvancementManager(_mainPlugin, teams);
        _processManager = new ProcessManager();
        _portalManager = new PortalManager(this, _processManager);
        _customVillagerManager = new CustomVillagerManager(_mainPlugin, customVillagers, this);
        _worldManager = new WorldManager(_mainPlugin, this, _portalManager, _processManager, _customVillagerManager);
        _chestManager = new StarterChestManager(_mainPlugin);
        _customItemManager = new CustomItemManager(_mainPlugin);
        blueTeam = new Team("blue", advancementManager, this);
        redTeam = new Team("red", advancementManager, this);
        craftX = new CraftX(advancementManager, _mainPlugin);
        xStacks = new XStacks(advancementManager, this, _mainPlugin);
        getGlowing = new GetGlowing(this, advancementManager);
        _animalSpawner = new AnimalSpawner(this, _worldManager, _processManager);
        netherManager = new NetherManager(this, _processManager, _worldManager);
        tickRate = 3;
        minWorldHeight = 94;
        normalVillagerAmount = 10;
        canProceed = true;
        isWorldGenerated = false;
        _passiveMobCap = 35;
        _generatorManager = new GeneratorManager();

    }
    public void Start() {
        Bukkit.broadcastMessage("Starting...");
//
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

        UpdateSpawns();
        _animalSpawner.SpawnAnimals();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!isRunning) {
                    this.cancel();
                }
                _animalSpawner.SpawnAnimals();
                _processManager.HandleProcesses(processes);
                _generatorManager.RenewGenerators(tickRate);
                _portalManager.PortalUpdate(portals, tickRate);
            }
        }.runTaskTimer(_mainPlugin, 0, tickRate);
    }
    public int FindInInventory(Inventory inventory, ItemStack item) {
        int slot = 0;
        for(ItemStack i : inventory.getContents()) {
            if(i != null) {
                if (i.equals(item)) {
                    return slot;
                }
            }
            slot++;
        }
        return -1;
    }
    public void GrantCompass(Player p, Team team) {
        try {
            if (team != null) {
                ItemStack spawnCompass = new ItemStack(Material.COMPASS);
                CompassMeta compassMeta = (CompassMeta) spawnCompass.getItemMeta();
                compassMeta.setLodestone(team.GetTeamWorld().GetWorldSpawn(this));
                compassMeta.setLodestoneTracked(false);
                compassMeta.setDisplayName("Spawn Compass");
                spawnCompass.setItemMeta(compassMeta);
                p.getInventory().addItem(spawnCompass);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void InitializeTasks() {
        //If statement checks if defaultConfiguration.json (which is used load advancement files that don't exit yet)
        if(Files.exists(Paths.get(advancementManager.GetAdvancementPath() + "default_configuration.json"))) {
            if(!advancementManager.customAdvancements.isEmpty()) {
                advancementManager.customAdvancements.clear();
            }

            //CustomAdvancement twoKills = new CustomAdvancement("twoKills", new ItemStack(Material.DIAMOND), advancementManager.customAdvancements);
            //CustomAdvancement sabotage = new CustomAdvancement("sabotage", new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement reach_level_X = new CustomAdvancement("reach_level", new ReachLevelX(this, advancementManager), new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement kill_two_players = new CustomAdvancement("kill_two_players", new TwoKillsTask(this, advancementManager), new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement kill_enderdragon = new CustomAdvancement("kill_enderdragon", new KillEnderdragon(this, advancementManager), new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement craft_item = new CustomAdvancement("craft_item", new CraftX(advancementManager, _mainPlugin),new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement get_glowing = new CustomAdvancement("get_glowing", new GetGlowing(this, advancementManager), new ItemStack(Material.DIAMOND), advancementManager);
            CustomAdvancement x_stacks = new CustomAdvancement("x_stacks", new XStacks(advancementManager,this, _mainPlugin), new ItemStack(Material.DIAMOND), advancementManager);


            for (CustomAdvancement a : advancementManager.GetCustomAdvancementList()) {
                a.LoadFile(advancementManager.GetDefaultConfiguration());
            }
            //advancementManager.RandomizeTasks();
            }
        else {
            Bukkit.broadcastMessage("ERROR: Missing mandatory files in tasks folder (default_configuration.json)");
        }

    }
    public void InitializeTeams() {
        AssignTeamWorlds();


        for(Team team : teams) {
            for(Player p : team.GetMembers()) {
                p.teleport(team.GetTeamWorld().GetWorldSpawn(this));
                advancementManager.GrantRootAdvancement(p);
                GrantCompass(p, team);
            }
        }
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
        List<Material> notSurfaceMaterials = new ArrayList<>();
        notSurfaceMaterials.add(Material.CAVE_AIR);
        notSurfaceMaterials.add(Material.AIR);
        notSurfaceMaterials.add(Material.WATER);
        notSurfaceMaterials.add(Material.VOID_AIR);
        notSurfaceMaterials.add(Material.LAVA);
        notSurfaceMaterials.add(Material.TALL_GRASS);

        Location scan = loc.clone();
        for(double i = maxY; i > minY; i--) {
            scan.setY(i);

            //Bukkit.broadcastMessage("current loc: " + scan.getX() + ", " + scan.getY() + ", " + scan.getZ() + ", block type: " + scan.getBlock().getType().name());
            Location block1Loc = scan.clone().add(0, 1, 0);
            Location block2Loc = scan.clone().add(0, 2, 0);
            //Block block2 = Bukkit.getWorld("void_world").getBlockAt((int)scan.getX(), (int)scan.getY() + 2, (int)scan.getZ());
            if(!notSurfaceMaterials.contains(scan.getBlock().getType())) {
                if(block1Loc.getBlock().getType() == Material.AIR && block2Loc.getBlock().getType() == Material.AIR) {
//                    Bukkit.broadcastMessage("block 1: " + block1Loc.getBlock().getType().name() + ", Location: " + block1Loc);
//                    Bukkit.broadcastMessage("block 2: " + block2Loc.getBlock().getType().name() + ", Location: " + block2Loc);

                    scan.setY(scan.getY() + 1);
                    //Bukkit.broadcastMessage("found loc: " + scan);
                    return scan;
                }
            }
            block1Loc = null;
            block2Loc = null;
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
        //Bukkit.broadcastMessage("customAdvancements size: " + advancementManager.customAdvancements.size());
        xStacks.WriteToXStacksFile();
        xStacks.UpdateDescription();
        craftX.WriteToCraftXFile();
        craftX.UpdateDescription();
        advancementManager.RandomizeTasks();

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

    public void Reset() {
        //List<Generator> _generators = _generatorManager.generators;
        for(Portal p : portals) {
            p.RemovePortal();
        }
        for(StarterChest chest : starterChestList) {
            chest.DestroyChest();
        }

        objects.addAll(portals);
        objects.addAll(_generatorManager.generators);
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
        _generatorManager.generators.clear();
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
    public void GenerateEndPortal(Location location) {
        BlockFace face;
        Location loc = location.clone();
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

    public void GenerateInvaderPortalFrame(Location location) {
        //BlockFace face;
        //loc.subtract(1, 0, 2);
        //face = BlockFace.SOUTH;
        Location loc = location.clone();
        loc.subtract(2, 0, 0);
        for(int e = 0; e < 2; e++) {
            for (int i = 0; i < 3; i++) {
                Location currentLoc = loc.clone();
                currentLoc.setY(loc.getY() + i);
                currentLoc.setX(loc.getX() + (e*4));
                currentLoc.getBlock().setType(Material.BEDROCK);
                currentLoc = null;
            }
        }
        loc.add(1, -1, 0);

        for(int e = 0; e < 2; e++) {
            for (int i = 0; i < 3; i++) {
                Location currentLoc = loc.clone();
                currentLoc.setX(loc.getX() + i);
                currentLoc.setY(loc.getY() + (e*4));
                currentLoc.getBlock().setType(Material.BEDROCK);
                currentLoc = null;

            }
        }
    }

    public int GetPassiveMobCap() {
        return _passiveMobCap;
    }
}
