package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomAdvancements.*;
import org.jacobn99.skyblockgambit.CustomItems.CustomItemManager;
import org.jacobn99.skyblockgambit.CustomItems.PortalOpener;
import org.jacobn99.skyblockgambit.CustomItems.RageSpell;
import org.jacobn99.skyblockgambit.CustomItems.VillagerTradeBoost;
import org.jacobn99.skyblockgambit.CustomVillagers.CustomVillagerManager;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;

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
        _xStacks = _gameManager.xStacks;
        world = Bukkit.getWorld("void_world");
        //_craftX = new CraftX(_gameManager, _advancementManager);
    }
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if(_gameManager.isRunning) {
            _xStacks.XStacksCheck(event);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(_gameManager.isRunning) {
            _killEnderdragon.KillEnderdragonCheck(event);
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
            _processManager.CreateProcess(_gameManager.processes, world.getFullTime() + 20, queueable);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = (Player) event.getEntity();
        Player killer = p.getKiller();

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
        if (_gameManager.isRunning && event.getPlayer().getItemInUse() != null) {
            _portalOpener.PortalOpenerCheck(event, _itemManager);
            _villagerTradeBoost.TradeBoostCheck(event, _itemManager);
            _rageSpell.RageSpellCheck(event, _itemManager);
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

//    @EventHandler
//    public void onBlockBreak(BlockBreakEvent event) {
//        Block block = event.getBlock();
//
//        Location borderLocation = _borderwall.GetBorderLocation();
//        double blockZ = block.getLocation().getZ();
//
//        if (blockZ >= borderLocation.getZ() - 0.5 && blockZ <= borderLocation.getZ() + 0.5) {
//            event.setCancelled(true);
//        }
//    }
}
