package org.jacobn99.skyblockgambit.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jacobn99.skyblockgambit.GameManager;

public class RageSpell {
    GameManager _gameManager;
    int effectDuration;
    public RageSpell(GameManager gameManager) {
        _gameManager = gameManager;
        effectDuration = 300;
    }
    public void RageSpellCheck(PlayerInteractEvent event, CustomItemManager itemManager) {
        Player p = event.getPlayer();
        if(_gameManager._customItemManager.AreEqual(event.getItem(), itemManager.GetCustomItem(itemManager.ItemNameToIndex("RAGE_SPELL")))) {
//        if (event.getItem().equals(itemManager.GetCustomItem(itemManager.ItemNameToIndex("RAGE_SPELL")))) {
            Bukkit.broadcastMessage("What the sigma?");
            event.setCancelled(true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, effectDuration, 2));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, effectDuration, 2));
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, effectDuration, 2));
            p.getInventory().remove(event.getItem());
        }
    }
}
