package org.jacobn99.skyblockgambit.CustomAdvancements;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TwoKillsTask {
GameManager _gameManager;
Map<Player, Integer> _killCounts;
    public TwoKillsTask(GameManager gameManager) {
        _gameManager = gameManager;
        _killCounts = _gameManager.killCounts;
    }

    public void TwoKillsCheck(Player killer) {
//        Player p = (Player) e.getEntity();
//        Player killer = p.getKiller();
//        if(_gameManager.isRunning && killer instanceof Player && _gameManager.participatingPlayers.contains(killer)) {
        if (_killCounts.get(killer) == 2) {
            Bukkit.broadcastMessage("Brootiotitjyj");
        }
        //}

    }
    public void KillCounter(Player killer) {
//        Player p = (Player) e.getEntity();
//        Player killer = p.getKiller();

        //if (_gameManager.isRunning && killer instanceof Player && _gameManager.participatingPlayers.contains(killer)) {
        _killCounts.replace(killer, _killCounts.get(killer) + 1);
        Bukkit.broadcastMessage(killer.getName() + " has " + _killCounts.get(killer) + " kills!");
        //}
    }
}
