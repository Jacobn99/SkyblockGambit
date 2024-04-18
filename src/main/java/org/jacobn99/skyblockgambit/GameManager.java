package org.jacobn99.skyblockgambit;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jacobn99.skyblockgambit.*;
import org.jacobn99.skyblockgambit.GeneratorInfo.DiamondGenerator;

import java.util.*;

public class GameManager {
    Location blueSpawn;
    Location redSpawn;
    long tickRate;
    public ArrayList<Generator> generatorList = new ArrayList();
    public List<Portal> portals = new ArrayList();
    public List<CustomVillager> customVillagers = new ArrayList();
    public Set<Player> blueTeam = new HashSet();
    public Set<Player> redTeam = new HashSet();
    JavaPlugin _mainPlugin;
    ArmorStand blueArmorStand;
    ArmorStand redArmorStand;

    PortalManager _portalManager;
    public GameManager(JavaPlugin mainPlugin) {
        _portalManager = new PortalManager();

        _mainPlugin = mainPlugin;
        tickRate = 3;
    }
    public void Start() {
        World world = Bukkit.getWorld("void_world");

        Generator gen1 = new DiamondGenerator(generatorList, new Location(world,124, -60, 163),
                new Location(world,122, -61, 163));
        Generator gen2 = new DiamondGenerator(generatorList, new Location(world,102, -60, 163),
                new Location(world,104, -61, 163));

        Portal portal1 = new Portal(portals, GetRedSpawn(),
                new Location(Bukkit.getWorld("void_world"), 113, -60, 168));
        Portal portal2 = new Portal(portals, GetBlueSpawn(),
                new Location(Bukkit.getWorld("void_world"), 112, -60, 0));

        UpdateSpawns();
        SpawnTeamVillagers();

        Borderwall _borderwall = new Borderwall(_mainPlugin);
        _borderwall.createBorder(GetBlueSpawn(), GetRedSpawn());

        new BukkitRunnable() {
            @Override
            public void run() {
                //UpdateSpawns();
                RenewGenerators(tickRate);
                _portalManager.PortalUpdate(portals);
            }
        }.runTaskTimer(_mainPlugin, 0, tickRate);
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
