package org.jacobn99.skyblockgambit;

import org.bukkit.entity.Player;
import org.jacobn99.skyblockgambit.CustomWorlds.CustomWorld;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Team {
    Set<Player> _members;
    CustomWorld _teamWorld;
    String _teamColor;

    public Team(CustomWorld teamWorld, String teamColor, List<Team> teams) {
        _members = new HashSet<>();
        _teamWorld = teamWorld;
        _teamColor = teamColor;
        teams.add(this);
    }
    public Set<Player> GetMembers() {
        return _members;
    }
    public void AddMember(Player player) {
        _members.add(player);
    }
    public CustomWorld GetTeamWorld() {
        return _teamWorld;
    }
    public void SetTeamWorld(CustomWorld _teamWorld) {
        this._teamWorld = _teamWorld;
    }
    public void RemoveMember(Player player) {
        _members.remove(player);
    }

    public String GetTeamColor() {
        return _teamColor;
    }

    public void SetTeamColor(String _teamColor) {
        this._teamColor = _teamColor;
    }
}
