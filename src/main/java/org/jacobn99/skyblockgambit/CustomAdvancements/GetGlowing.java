package org.jacobn99.skyblockgambit.CustomAdvancements;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.jacobn99.skyblockgambit.GameManager;

public class GetGlowing {
    GameManager _gameManager;
    //CustomAdvancement _advancement;
    private AdvancementManager _advancementManager;
    public GetGlowing(GameManager gameManager, AdvancementManager advancementManager) {
        _gameManager = gameManager;
        _advancementManager = advancementManager;
        //_advancement = _advancementManager.GetAdvancement("kill_enderdragon");

    }

    public void GetGlowingCheck(EntityPotionEffectEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if(p.isGlowing()) {
                _advancementManager.GrantTeamAdvancement(p, _advancementManager.GetAdvancement("get_glowing"), true);
            }
        }
    }
}
