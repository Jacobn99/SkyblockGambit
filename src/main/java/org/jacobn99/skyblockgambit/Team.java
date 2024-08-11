package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jacobn99.skyblockgambit.CustomAdvancements.CustomAdvancement;
import org.jacobn99.skyblockgambit.CustomWorlds.CustomWorld;
import org.jacobn99.skyblockgambit.Portals.Portal;

import java.util.*;

public class Team {
    Set<Player> _members;
    CustomWorld _teamWorld;
    String _teamColor;
    Portal _teamPortal;
    Set<Player> _participatingPlayers;
    List<Team> _teams;
    GameManager _gameManager;
    List<CustomAdvancement> _finishedTasks;
    Location _netherSpawn;
    public Inventory killsInventory;
    private int _generatorCount;


    public Team(String teamColor, GameManager gameManager) {
        _gameManager = gameManager;
        _netherSpawn = null;
        _finishedTasks = new ArrayList<>();
        _members = new HashSet<>();
        _teamWorld = null;
//        _teamWorld = teamWorld;
        _teamColor = teamColor;
        _teams = _gameManager.teams;
        _participatingPlayers = _gameManager.participatingPlayers;
        _teams.add(this);
        _generatorCount = 0;
        killsInventory = Bukkit.createInventory(null, 27, "PVP Rewards");

    }

    public int GetGeneratorCount() {
        return _generatorCount;
    }
    public void AddToGeneratorCount() {
        _generatorCount += 1;
    }

    public boolean AreTasksDone() {
        if(_finishedTasks.size() == _gameManager.advancementManager.GetMaxTasks() - 1) {
            return true;
        }
        return false;
    }
    public List<CustomAdvancement> GetFinishedTasks() {
        return _finishedTasks;
    }

    public void AddFinishedTask(CustomAdvancement advancement) {
        this._finishedTasks.add(advancement);
        Bukkit.broadcastMessage(_finishedTasks.size() + " tasks completed" + " vs " + _gameManager.advancementManager.GetMaxTasks() + " max tasks");
        if(AreTasksDone() && _gameManager.isRunning) {
            Location portalSpawn = this.GetTeamWorld().GetWorldSpawn(_gameManager).clone();
            portalSpawn.add(0, 10, 0);
            //Bukkit.broadcastMessage("Bazinga!!!!!!!!");
            _gameManager.GenerateEndPortal(portalSpawn);
        }
    }
    public void RemoveFinishedTask(CustomAdvancement advancement) {
        this._finishedTasks.remove(advancement);
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

    public Location GetNetherSpawn() {
        return _netherSpawn;
    }

    public void SetNetherSpawn(Location _netherSpawn) {
        this._netherSpawn = _netherSpawn;
    }
}
