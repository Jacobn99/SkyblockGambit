package org.jacobn99.skyblockgambit.CustomAdvancements;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jacobn99.skyblockgambit.GameManager;

import java.util.List;
import java.util.Map;

public class TwoKillsTask {
GameManager _gameManager;
Map<Player, Integer> _killCounts;
CustomAdvancement _advancement;
private AdvancementManager _advancementManager;
    public TwoKillsTask(GameManager gameManager, AdvancementManager advancementManager) {
        _gameManager = gameManager;
        _killCounts = _gameManager.killCounts;
        _advancementManager = advancementManager;
        //_advancement = _advancementManager.GetAdvancement("twoKills");
    }

    public void TwoKillsCheck(Player killer) {
//        Player p = (Player) e.getEntity();
//        Player killer = p.getKiller();
        if(_gameManager.isRunning && killer instanceof Player && _gameManager.participatingPlayers.contains(killer)) {
            //Bukkit.broadcastMessage("kills % 2: " + (_killCounts.get(killer) % 2));
            if (_killCounts.get(killer) % 2 == 0) {
                _advancement = _advancementManager.GetAdvancement("kill_two_players");
                if(_advancement != null) {
//                    Bukkit.broadcastMessage("granting advancement");
                    _advancementManager.GrantTeamAdvancement(killer, _advancement, true);
                    //_advancement.GrantAdvancement(killer, false);
                }
            }
        }
    }
    public boolean IsKillFromOtherTeam(Player killer, Player victim) {
        if(_gameManager.FindPlayerTeam(killer) != null && _gameManager.FindPlayerTeam(victim) != null) {
            if(!_gameManager.FindPlayerTeam(killer).GetMembers().contains(victim)) {
                return true;
            }
        }
        return false;
    }
    public void AddToKillCount(Player killer) {
//        Player p = (Player) e.getEntity();
//        Player killer = p.getKiller();

        //if (_gameManager.isRunning && killer instanceof Player && _gameManager.participatingPlayers.contains(killer)) {
        _killCounts.replace(killer, _killCounts.get(killer) + 1);
        Bukkit.broadcastMessage(killer.getName() + " has " + _killCounts.get(killer) + " kills!");
        //}
    }
}
