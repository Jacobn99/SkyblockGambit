package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jacobn99.skyblockgambit.CustomWorlds.CustomWorld;
import org.jacobn99.skyblockgambit.Portals.Portal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Team {
    Set<Player> _members;
    CustomWorld _teamWorld;
    String _teamColor;
    Portal _teamPortal;
    Set<Player> _participatingPlayers;
    List<Team> _teams;
    GameManager _gameManager;
    public Team(String teamColor, GameManager gameManager) {
        _gameManager = gameManager;
        _members = new HashSet<>();
        _teamWorld = null;
//        _teamWorld = teamWorld;
        _teamColor = teamColor;
        _teams = _gameManager.teams;
        _participatingPlayers = _gameManager.participatingPlayers;
        _teams.add(this);
    }
    public Set<Player> GetMembers() {
        return _members;
    }
    public void AddMember(Player player) {
        _members.add(player);
        _participatingPlayers.add(player);
        _gameManager.killCounts.put(player, 0);

    }
    public CustomWorld GetTeamWorld() {
        if(_teamWorld == null) {
            Bukkit.broadcastMessage("ERROR: Team World is null");
        }
        return _teamWorld;
    }
    public void SetTeamWorld(CustomWorld _teamWorld) {
        this._teamWorld = _teamWorld;
    }
    public void RemoveMember(Player player) {
        _members.remove(player);
        for(Team team : _teams) {
            if(team.GetMembers().contains(player)) {
                return;
            }
        }
        _participatingPlayers.remove(player);
    }

    public String GetTeamColor() {
        return _teamColor;
    }

    public void SetTeamColor(String _teamColor) {
        this._teamColor = _teamColor;
    }

}
