package org.jacobn99.skyblockgambit.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Team;

public class PortalOpener {
    GameManager _gameManager;
    public PortalOpener(GameManager gameManager) {
        _gameManager = gameManager;
    }
    public void PortalOpenerCheck(PlayerInteractEvent event, CustomItemManager itemManager) {
        Player p = event.getPlayer();
        if (event.getItem() != null) {
            if (_gameManager._customItemManager.AreEqual(event.getItem(), itemManager.GetCustomItem(itemManager.ItemNameToIndex("PORTAL_OPENER")))) {
                Bukkit.broadcastMessage("What the sigma?");
                event.setCancelled(true);

                Team team = _gameManager.FindPlayerTeam(p);

                if (team != null) {
                    if (team.GetTeamWorld().GetWorldPortal() != null) {
                        team.GetTeamWorld().GetWorldPortal().Activate();
                        p.getInventory().remove(event.getItem());
                    }
                }
            }
        }
    }
}
