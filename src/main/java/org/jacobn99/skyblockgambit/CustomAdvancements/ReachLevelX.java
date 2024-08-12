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
        _advancement = null;
    }
    private CustomAdvancement GetReachAdvancement() {
        if(_advancement == null) {
            _advancement = _advancementManager.GetAdvancement("reach_level");
        }
        return _advancement;
    }

    public void ReachLevelXCheck(Player p) {
        //Bukkit.broadcastMessage("Exp level: " + p.getLevel());
        CustomAdvancement advancement = GetReachAdvancement();
        if (_advancement != null) {
            //Bukkit.broadcastMessage("got here");
            if (p.getLevel() >= _level) {
                //Bukkit.broadcastMessage("Swag");

                _advancementManager.GrantTeamAdvancement(p, _advancement, true);
                //_advancement.GrantAdvancement(p, false);
            }

        }
    }
}
