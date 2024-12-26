package org.jacobn99.skyblockgambit;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomAdvancements.*;
import org.jacobn99.skyblockgambit.CustomItems.*;
import org.jacobn99.skyblockgambit.CustomVillagers.CustomVillagerManager;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;

import java.util.HashSet;
import java.util.Set;

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
        _twoKillsTask = new TwoKillsTask(_gameManager, _advancementManager);
        _reachLevelX = new ReachLevelX(_gameManager, _advancementManager);
        _killEnderdragon = new KillEnderdragon(_gameManager, _advancementManager);
        _generatorContructor = new GeneratorConstructor(_gameManager._generatorManager.generators, _gameManager._generatorManager, _itemManager, _gameManager);
        _xStacks = _gameManager.xStacks;
        _netherManager = _gameManager.netherManager;
        _getGlowing = _gameManager.getGlowing;
        _portalManager = _gameManager.portalManager;
        world = Bukkit.getWorld("void_world");
    }
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if(_gameManager.isRunning) {
            _generatorContructor.SelectGeneratorCheck(event);
            _xStacks.XStacksCheck(event);
            if(_gameManager.nonClickableInventories.contains(event.getInventory())) {
                event.setCancelled(true);
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
        Player p = event.getPlayer();
        Team team = _gameManager.FindPlayerTeam(p);
        Queueable queueable = () -> _gameManager.GrantCompass(p,team);
        if(_gameManager.isRunning && team != null) {
            _processManager.CreateProcess(world.getFullTime() + 20, queueable);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = (Player) event.getEntity();
        Player killer = p.getKiller();

        if(_portalManager.invaders.containsKey(p)) {
            _portalManager.invaders.remove(p);
        }

        if(_gameManager.isRunning && killer instanceof Player && _gameManager.participatingPlayers.contains(killer)) {
            if(_twoKillsTask.IsKillFromOtherTeam(killer, p)) {
                _gameManager.FindPlayerTeam(p).killsInventory.addItem(_itemManager.GetCustomItem(_itemManager.ItemNameToIndex("KILL_SKULL")));
                _twoKillsTask.AddToKillCount(killer);
            }
            _twoKillsTask.TwoKillsCheck(killer);
        }
        _gameManager.UpdateSpawns();

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
        MerchantRecipe recipe = _villagerManager.MakeTradeCheaper(event.getRecipe());
        event.setRecipe(recipe);
    }
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
//        Villager villagerReference;
//        Villager trader;
        if(event.getInventory() instanceof MerchantInventory) {
            MerchantInventory inventory = (MerchantInventory) event.getInventory();
            Merchant merchant = inventory.getMerchant();
            if(event.getInventory().getHolder() != null)  {
                for(MerchantRecipe recipe : merchant.getRecipes()) {
                    //Bukkit.broadcastMessage("Recipe: " + recipe.getIngredients() + "-> " + recipe.getResult());
                    recipe.setMaxUses(10000);
                }
//                List<MerchantRecipe> newRecipes = new ArrayList<>();
//
//                ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 30);
//
//                MerchantRecipe firstRecipe = new MerchantRecipe(cobblestone, 10000);
//
//                firstRecipe.addIngredient(new ItemStack(Material.EMERALD));
//
//                newRecipes.add(firstRecipe);
//                merchant.setRecipes(newRecipes);

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

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (event.getEntity().getItemStack().equals(_gameManager.delayItem)) {
//            ResetCommandFeedback();
        }
    }
}
