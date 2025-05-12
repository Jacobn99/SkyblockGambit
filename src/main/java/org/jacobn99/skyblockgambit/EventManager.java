package org.jacobn99.skyblockgambit;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomAdvancements.*;
import org.jacobn99.skyblockgambit.CustomItems.*;
import org.jacobn99.skyblockgambit.CustomVillagers.CustomVillager;
import org.jacobn99.skyblockgambit.CustomVillagers.CustomVillagerManager;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;

import java.util.Collection;

public class EventManager implements Listener {
    JavaPlugin _mainPlugin;
    CustomItemManager _itemManager;
    Borderwall _borderwall;
    GameManager _gameManager;
    PortalOpener _portalOpener;
    RageSpell _rageSpell;
    VillagerTradeBoost _villagerTradeBoost;
    TwoKillsTask _twoKillsTask;
    ReachLevelX _reachLevelX;
    AdvancementManager _advancementManager;
    KillEnderdragon _killEnderdragon;
    CustomVillagerManager _villagerManager;
    ProcessManager _processManager;
    private XStacks _xStacks;
    private GetGlowing _getGlowing;
    private GeneratorConstructor _generatorContructor;
    private BundleInsurance _bundleInsurance;
    private NukeSheepItem _nukeSheepItem;
    private NetherManager _netherManager;
    private PortalManager _portalManager;

    World world;
    public EventManager(JavaPlugin mainPlugin, GameManager gameManager) {
        _mainPlugin = mainPlugin;
        _itemManager = new CustomItemManager(_mainPlugin);
        _gameManager = gameManager;
        _advancementManager = _gameManager.advancementManager;
        _villagerManager = _gameManager._customVillagerManager;
        _processManager = _gameManager._processManager;
        _borderwall = new Borderwall(_mainPlugin, _gameManager);

        _portalOpener = new PortalOpener(_gameManager);
        _villagerTradeBoost = new VillagerTradeBoost(_gameManager);
        _rageSpell = new RageSpell(_gameManager);
        _generatorContructor = new GeneratorConstructor(_gameManager._generatorManager.generators, _gameManager._generatorManager, _itemManager, _gameManager);

        _nukeSheepItem = _gameManager.nukeSheepItem;

        _twoKillsTask = new TwoKillsTask(_gameManager, _advancementManager);
        _reachLevelX = new ReachLevelX(_gameManager, _advancementManager);
        _killEnderdragon = new KillEnderdragon(_gameManager, _advancementManager);
        _xStacks = _gameManager.xStacks;
        _bundleInsurance = _gameManager.bundleInsurance;

        _netherManager = _gameManager.netherManager;
        _getGlowing = _gameManager.getGlowing;
        _portalManager = _gameManager.portalManager;
        world = Bukkit.getWorld("void_world");
    }
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        InventoryAction action = event.getAction();
        if(_gameManager.isRunning) {
            _generatorContructor.SelectGeneratorCheck(event);
            _xStacks.XStacksCheck(event);
            if (_gameManager.nonClickableInventories.contains(event.getInventory())) {
                event.setCancelled(true);
            }
            if (_gameManager.nonAdditiveInventories.contains(event.getInventory()) && event.isShiftClick()) {
                event.setCancelled(true);
            }
            if (action.equals(InventoryAction.PLACE_ALL) ||
                    action.equals(InventoryAction.PLACE_ONE) ||
                    action.equals(InventoryAction.PLACE_SOME) ||
                    action.equals(InventoryAction.SWAP_WITH_CURSOR)) {
                if (_gameManager.nonAdditiveInventories.contains(event.getClickedInventory())) {
                    event.setCancelled(true);
                }
                else if(_bundleInsurance.isBundleInsurance(event.getCurrentItem()) &&
                        !_bundleInsurance.isUnownedBundleInsurance(event.getCurrentItem())) {
                    ItemStack bundle = event.getCurrentItem();
                    Player clicker = (Player) event.getWhoClicked();
                    Player owner = _bundleInsurance.GetOwner(bundle);

                    if((owner != clicker && owner != null)) {
                        event.setCancelled(true);
                        _processManager.CreateProcess(_processManager.getCurrentTime() + 1,
                                ()->event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.LEATHER)));
                    }
                    else if (_bundleInsurance.HasDuplicates(owner)) {
                        event.setCancelled(true);
                        _processManager.CreateProcess(_processManager.getCurrentTime() + 1,
                                ()->event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.LEATHER)));
                    }
                    else {
                        _processManager.CreateProcess(_processManager.getCurrentTime() + 1,
                                ()->_bundleInsurance.UpdateMap(owner, bundle));
                    }

//                    if (clicker == owner && (Player) event.getInventory().getHolder() == clicker
//                            && event.getClickedInventory() != clicker.getInventory()) {
//                        clicker.sendMessage("Can't move your bundle insurance out of your inventory");
//                        event.setCancelled(true);
////                    }
//                    }
                }
            }
        }
    }
    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if(_gameManager.isRunning) {
            _getGlowing.GetGlowingCheck(event);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(_gameManager.isRunning) {
            _killEnderdragon.KillEnderdragonCheck(event);
            _villagerManager.VillagerDeathCheck(event);
        }

    }

    @EventHandler
    public void onEntityRemoval(EntityRemoveEvent event) {
        if(_gameManager._animalSpawner._hostileTypes.contains(event.getEntity().getType()) &&
        event.getEntity().getScoreboardTags().contains("spawned") && _gameManager.naturalHostileCount > 0) {
            _gameManager.naturalHostileCount -= 1;
        }

    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if(_gameManager.isRunning) {
            _gameManager.craftX.CraftXCheck(event);
        }
    }
    @EventHandler
    public void onLevelUp(PlayerExpChangeEvent event) {
        //Bukkit.broadcastMessage("level increase");
        Player p = event.getPlayer();
        if(_gameManager.isRunning) {
            _reachLevelX.ReachLevelXCheck(p);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if(_gameManager.isRunning) {
            Player p = event.getPlayer();
            Team team = _gameManager.FindPlayerTeam(p);
            Queueable queueable = () -> _gameManager.GrantCompass(p, team);
            if (_gameManager.isRunning && team != null) {
                _processManager.CreateProcess(_processManager.getCurrentTime() + 2,
                        ()-> p.teleport(team.GetTeamWorld().GetWorldSpawn(_gameManager)));
                _processManager.CreateProcess(_processManager.getCurrentTime() + 10, queueable);
                if(_bundleInsurance.inventories.containsKey(p)) {
                    _processManager.CreateProcess(_processManager.getCurrentTime() + 10,
                            () -> _bundleInsurance.GrantOwnedBundle(p));
                }
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getEntity() instanceof Slime) {
            event.setCancelled(true);
        }
        if(event.getEntity().getWorld() == _gameManager._world &&
                _gameManager._animalSpawner._hostileTypes.contains(event.getEntity().getType())) {
            if (_gameManager.naturalHostileCount >= _gameManager.GetNaturalHostileCap()) {
                if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                    event.setCancelled(true);
                }
            } else {
                event.getEntity().addScoreboardTag("spawned");
                _gameManager.naturalHostileCount += 1;
            }
//            Bukkit.broadcastMessage("naturalHostileCount: " + _gameManager.naturalHostileCount);
//        }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(_gameManager.isRunning) {
            Player p = (Player) event.getEntity();
            Player killer = p.getKiller();
            _processManager.CreateProcess(_processManager.getCurrentTime() + 2,
                    ()-> _bundleInsurance.DeathCheck(p, false));
            if (_portalManager.invaders.containsKey(p)) {
                _portalManager.invaders.remove(p);
            }

            if (_gameManager.isRunning && killer instanceof Player &&
                    _gameManager.participatingPlayers.contains(killer)) {
                if (_twoKillsTask.IsKillFromOtherTeam(killer, p)) {
                    _gameManager.FindPlayerTeam(killer).killsInventory.addItem(
                            _itemManager.GetCustomItem(_itemManager.ItemNameToIndex("KILL_SKULL")));
                    _twoKillsTask.AddToKillCount(killer);
                }
                _twoKillsTask.TwoKillsCheck(killer);
            }
            _gameManager.UpdateSpawns();
        }

    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        org.bukkit.entity.Entity attacker = event.getDamager();
        org.bukkit.entity.Entity victim = event.getEntity();

        if(victim instanceof Player) {
            Player player = (Player) victim;
            boolean isPvpDeath = false;

            if (attacker != null && attacker instanceof Player &&
                    _gameManager.FindPlayerTeam((Player) attacker) != _gameManager.FindPlayerTeam(player)) {
                isPvpDeath = true;
            }
            _processManager.CreateProcess(_processManager.getCurrentTime() + 2,
                    ()-> _bundleInsurance.DeathCheck(player, true));
            _bundleInsurance.ResetBundleInsurance(player);

        }
        if(victim instanceof Villager && victim.getScoreboardTags().contains("Customized") &&
            attacker instanceof Zombie && ((Villager) victim).getHealth() <= event.getDamage() &&
            _gameManager.isRunning) {
            CustomVillager v = _villagerManager.GetFromCustoms((Villager)victim);
            if(v != null) {
                Villager newVillager = _villagerManager.SpawnVillager(v.GetSpawnLocation(),
                        v.GetVillager().getProfession());
                _villagerManager.ApplyTraits((Villager)victim, newVillager);
                victim.remove();
                v.SetVillager(newVillager);
                event.setCancelled(true);

            }
        }
    }

    public void ZombieKillVillagerTest(EntityDamageByEntityEvent event) {
        org.bukkit.entity.Entity attacker = event.getDamager();
        org.bukkit.entity.Entity victim = event.getEntity();

        if(victim instanceof Villager) {
            Bukkit.broadcastMessage((victim instanceof Villager) + ", " +
                    (victim.getScoreboardTags().contains("Customized") +
                    ", " + (attacker instanceof Zombie)) + ", " + (((Villager) victim).getHealth() <= event.getDamage()));
        }
        else Bukkit.broadcastMessage("Not villager");
    }
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        //Bukkit.broadcastMessage("isRunning: " + _gameManager.isRunning + " and itemInUse: " + event.getPlayer().getInventory().getItemInMainHand());
        if (_gameManager.isRunning && event.getPlayer().getInventory().getItemInMainHand() != null &&
                (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
//            Bukkit.broadcastMessage("item: " + event.getPlayer().getInventory().getItemInMainHand());
            _generatorContructor.GeneratorConstructorCheck(event);
            _portalOpener.PortalOpenerCheck(event, _itemManager);
            _villagerTradeBoost.TradeBoostCheck(event, _itemManager);
            _rageSpell.RageSpellCheck(event, _itemManager);
            _nukeSheepItem.NukeSheepCheck(event, _itemManager);
            _bundleInsurance.BundleCheck(event);

            Player p = event.getPlayer();
            ItemStack mainItem = event.getItem();
            if(_bundleInsurance.isBundleInsurance(mainItem) &&
                    !_bundleInsurance.isUnownedBundleInsurance(mainItem)) {
                _bundleInsurance.UpdateMap(_bundleInsurance.GetOwner(mainItem), mainItem);
            }

        }
    }
    @EventHandler
    public void onPortalEntry(PlayerPortalEvent event) {
        //Bukkit.broadcastMessage("portal entered");
        Player p = event.getPlayer();
        //event.setCancelled(true);

        if(_gameManager.isRunning) {
            _netherManager.netherPortalLocations.put(p, p.getLocation());
            _netherManager.CorrectNetherSpawning(event);
        }
    }

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        MerchantRecipe recipe = _villagerManager.MakeTradeCheaper(event.getRecipe(), 0.5);
        event.setRecipe(recipe);
    }
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if(event.getInventory() instanceof MerchantInventory) {
            MerchantInventory inventory = (MerchantInventory) event.getInventory();
            Merchant merchant = inventory.getMerchant();
            if(event.getInventory().getHolder() != null)  {
                for(MerchantRecipe recipe : merchant.getRecipes()) {
                    //Bukkit.broadcastMessage("Recipe: " + recipe.getIngredients() + "-> " + recipe.getResult());
                    recipe.setMaxUses(10000);
                    recipe.setPriceMultiplier(0);
                }
            }
            else {
                return;
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(_gameManager.isRunning) {
            if(_netherManager.ProtectNetherSpawns(event.getBlock())) {
                event.getPlayer().sendMessage("Can't place/break blocks in a " + _netherManager.protectionLength/2 + " by " + _netherManager.protectHeight + " by " + _netherManager.protectionLength/2 + " area around team portal");
                event.setCancelled(true);
            }

        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(_gameManager.isRunning) {
            if(_netherManager.ProtectNetherSpawns(event.getBlock())) {
                event.getPlayer().sendMessage("Can't place/break blocks in a " + _netherManager.protectionLength/2 + " by " + _netherManager.protectHeight + " by " + _netherManager.protectionLength/2 + " area around team portal");
                event.setCancelled(true);
            }

        }
    }

}
