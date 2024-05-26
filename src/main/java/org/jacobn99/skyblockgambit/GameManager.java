package org.jacobn99.skyblockgambit;

import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jacobn99.skyblockgambit.GeneratorInfo.DiamondGenerator;

import java.util.*;

public class GameManager {
    Location blueSpawn;
    Location redSpawn;
    long tickRate;
    public ArrayList<Generator> generatorList = new ArrayList();
    public List<Portal> portals = new ArrayList();
    public List<CustomVillager> customVillagers = new ArrayList();
    public List<StarterChest> starterChestList = new ArrayList<>();
    public Set<Set<Player>> teams = new HashSet<>();
    public Set<Player> blueTeam;
    public Set<Player> redTeam;
    Portal redPortal;
    Portal bluePortal;
    HashMap<Long, Queueable> processes;
    JavaPlugin _mainPlugin;
    ArmorStand blueArmorStand;
    ArmorStand redArmorStand;
    PortalManager _portalManager;
    public GameManager(JavaPlugin mainPlugin) {
        blueTeam = new HashSet();
        redTeam = new HashSet();
        processes = new HashMap<>();
        _portalManager = new PortalManager();
        teams.add(blueTeam);
        teams.add(redTeam);

        _mainPlugin = mainPlugin;
        tickRate = 3;
    }
    public void Start() {
        StarterChestManager _chestManager = new StarterChestManager(_mainPlugin);

        World world = Bukkit.getWorld("void_world");
        //StarterChest chest1 = new StarterChest()

        Generator gen1 = new DiamondGenerator(generatorList, new Location(world,124, -60, 163),
                new Location(world,122, -61, 163));
        Generator gen2 = new DiamondGenerator(generatorList, new Location(world,102, -60, 163),
                new Location(world,104, -61, 163));

        redPortal = new Portal(portals, GetRedSpawn(),
                new Location(Bukkit.getWorld("void_world"), 113, -60, 168));
        bluePortal = new Portal(portals, GetBlueSpawn(),
                new Location(Bukkit.getWorld("void_world"), 112, -60, 0));

        StarterChest blueChest = new StarterChest(new Location(world, 113, -60, 160), _chestManager.GetInventory(), starterChestList);
        blueChest.CreateChest();

        StarterChest redChest = new StarterChest(new Location(world, 112, -60, 7), _chestManager.GetInventory(), starterChestList);
        redChest.CreateChest();


        UpdateSpawns();
        SpawnTeamVillagers();

        Borderwall _borderwall = new Borderwall(_mainPlugin);
        _borderwall.createBorder(GetBlueSpawn(), GetRedSpawn());

        new BukkitRunnable() {
            @Override
            public void run() {
                //UpdateSpawns();
                HandleProcesses();
                RenewGenerators(tickRate);
                _portalManager.PortalUpdate(portals);
            }
        }.runTaskTimer(_mainPlugin, 0, tickRate);
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
}
