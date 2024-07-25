package org.jacobn99.skyblockgambit.CustomAdvancements;

import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jacobn99.skyblockgambit.GameManager;

public class KillEnderdragon {
    GameManager _gameManager;
    CustomAdvancement _advancement;
    private AdvancementManager _advancementManager;
    public KillEnderdragon(GameManager gameManager, AdvancementManager advancementManager) {
        _gameManager = gameManager;
        _advancementManager = advancementManager;
        //_advancement = _advancementManager.GetAdvancement("kill_enderdragon");

    }

    public void KillEnderdragonCheck(EntityDeathEvent event) {
        //Bukkit.broadcastMessage("Exp level: " + p.getLevel());
        if(event.getEntity().getKiller() != null && event.getEntity() instanceof EnderDragon) {
            Player p = event.getEntity().getKiller();
            _advancementManager.GrantTeamAdvancement(p, _advancementManager.GetAdvancement("kill_enderdragon"));
        }
    }
}
