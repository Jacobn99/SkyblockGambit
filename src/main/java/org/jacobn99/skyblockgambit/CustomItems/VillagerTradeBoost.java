package org.jacobn99.skyblockgambit.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Team;

public class VillagerTradeBoost {
    GameManager _gameManager;
    int effectDuration;
    int amplifier;
    public VillagerTradeBoost(GameManager gameManager) {
        _gameManager = gameManager;
        effectDuration = 1200;
        amplifier = 3;
    }
    public void TradeBoostCheck(PlayerInteractEvent event, CustomItemManager itemManager) {
        Player p = event.getPlayer();
        if(event.getItem() != null) {
            if(_gameManager._customItemManager.AreEqual(event.getItem(), itemManager.GetCustomItem(itemManager.ItemNameToIndex("TRADE_BOOST")))) {

//            if (event.getItem().equals(itemManager.GetCustomItem(itemManager.ItemNameToIndex("TRADE_BOOST")))) {
                Bukkit.broadcastMessage("What the sigma?");
                p.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, effectDuration, amplifier));
            }
        }
    }
}
