package org.jacobn99.skyblockgambit.CustomAdvancements;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.jacobn99.skyblockgambit.GameManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;

public class GetGlowing implements AdvancementType{
    GameManager _gameManager;
    World _world;
    //CustomAdvancement _advancement;
    private AdvancementManager _advancementManager;
    public GetGlowing(GameManager gameManager, AdvancementManager advancementManager) {
        _gameManager = gameManager;
        _advancementManager = advancementManager;
        _world = Bukkit.getWorld("void_world");
    }

    public void GetGlowingCheck(EntityPotionEffectEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            _gameManager._processManager.CreateProcess(_gameManager.processes,
                    _world.getFullTime() + 5, () -> CheckForEffect(p));
        }
    }

    private void CheckForEffect(Player p) {
        if(p.isGlowing()) {
            _advancementManager.GrantTeamAdvancement(p, _advancementManager.GetAdvancement("get_glowing"), true);
        }
    }

    @Override
    public Material GetSymbol() {
        return Material.SPECTRAL_ARROW;
    }

    @Override
    public String GetDescription() {
        return "Get the glowing effect";
    }
}
