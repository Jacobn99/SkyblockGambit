package org.jacobn99.skyblockgambit.CustomAdvancements;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jacobn99.skyblockgambit.GameManager;

import java.util.Map;

public class ReachLevelX {
    GameManager _gameManager;
    CustomAdvancement _advancement;
    private int _level;
    private AdvancementManager _advancementManager;
    public ReachLevelX(GameManager gameManager, AdvancementManager advancementManager) {
        _gameManager = gameManager;
        _advancementManager = advancementManager;
        _level = 10;
        _advancement = _advancementManager.GetAdvancement("reach_level");
    }

    public void ReachLevelXCheck(Player p) {
        //Bukkit.broadcastMessage("Exp level: " + p.getLevel());
        if(p.getLevel() >= _level && _advancement != null) {
            //Bukkit.broadcastMessage("Swag");

            _advancementManager.GrantTeamAdvancement(p, _advancement);
            //_advancement.GrantAdvancement(p, false);
        }

    }
}
