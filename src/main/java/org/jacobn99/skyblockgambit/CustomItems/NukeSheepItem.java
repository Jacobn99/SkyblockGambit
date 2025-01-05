package org.jacobn99.skyblockgambit.CustomItems;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.NukeSheep;

import java.util.ArrayList;
import java.util.List;

public class NukeSheepItem {
    GameManager _gameManager;
    private List<NukeSheep> _nukeSheeps;
    private long _ticksToBlow;
    private float explosionSize;
    public NukeSheepItem(GameManager gameManager) {
        _gameManager = gameManager;
        _nukeSheeps = _gameManager.nukeSheeps;
        _ticksToBlow = 100;
        explosionSize = 50;
    }
    public void NukeSheepCheck(PlayerInteractEvent event, CustomItemManager itemManager) {
        Player p = event.getPlayer();
        ItemStack customItem = itemManager.GetCustomItem(itemManager.ItemNameToIndex("NUKE_SHEEP"));
        if (event.getItem() != null) {
//            _gameManager._customItemManager.TestAreEqual(event.getItem(), itemManager.GetCustomItem(itemManager.ItemNameToIndex("NUKE_SHEEP")), false);
            if (_gameManager._customItemManager.AreEqual(event.getItem(), customItem, false)) {
//                Bukkit.broadcastMessage("What the sigma?");
                event.setCancelled(true);
                Location loc = event.getClickedBlock().getLocation();
                Location locUp = loc.clone().add(0,1,0);
                if(loc != null && locUp.getBlock().getType() == Material.AIR) {
                    loc.setY(loc.getY() + 1);
                    SpawnNuke(loc);
                    p.getInventory().removeItem(customItem);
                }
            }
        }
    }

    public void SpawnNuke(Location loc) {
        World world = loc.getWorld();
        Sheep sheep = (Sheep) world.spawnEntity(loc, EntityType.SHEEP);
        sheep.setAI(false);
        sheep.setInvulnerable(true);
        sheep.setColor(DyeColor.WHITE);
        sheep.addScoreboardTag("disposable");
        NukeSheep nukeSheep = new NukeSheep(sheep, _ticksToBlow, _gameManager._processManager, _nukeSheeps);
    }

    public void NukeSheepUpdate(long tickRate) {
        List<NukeSheep> trashSheeps = new ArrayList<>();
        for(NukeSheep nukeSheep : _nukeSheeps) {
            Sheep sheep = nukeSheep.get_sheep();
            if(sheep.isDead()) {
                _nukeSheeps.remove(nukeSheep);
            }
            else {
                long timeLeft = nukeSheep.get_timeLeft();
                long timeToFlash = nukeSheep.get_timeToFlash();
//                Bukkit.broadcastMessage("time left to flash: " + timeToFlash + ", delay: " + nukeSheep.get_flashDelay());
                if(timeToFlash <= 0) {
                    nukeSheep.Flash();
                    nukeSheep.set_timeToFlash(nukeSheep.get_flashDelay());
//                    Bukkit.broadcastMessage("time to flash: " + nukeSheep.get_timeToFlash());
                }
                else {
                    nukeSheep.set_timeToFlash(timeToFlash - tickRate);

                }
                if(timeLeft <= 0) {
                    sheep.getWorld().createExplosion(sheep.getLocation(), explosionSize);
                    trashSheeps.add(nukeSheep);
                    sheep.remove();
                }
                nukeSheep.set_timeLeft(timeLeft - tickRate);
//                nukeSheep.set_timeToFlash(timeToFlash - tickRate);
            }
        }
        for(NukeSheep nukeSheep : trashSheeps) {
            _nukeSheeps.remove(nukeSheep);
        }
    }
//    public void FlashRed(Sheep sheep) {
//
//    }
}
