package org.jacobn99.skyblockgambit;

import org.bukkit.plugin.java.JavaPlugin;

public final class SkyblockGambit extends JavaPlugin {
    private String[] commandNames = {"start", "debug", "t", "set_spawn", "spawn_villager", "set_custom_item",
            "add_custom_item", "get_custom_item", "set_starter_chest"};
    GameManager _gameManager = new GameManager(this);
    EventManager _eventManager = new EventManager(this, _gameManager);

    @Override
    public void onEnable() {
        //ConfigManager _configManager = new ConfigManager(this);
        saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(_eventManager, this);
        // Plugin startup logic
        CommandExecuter commandExecuter = new CommandExecuter(this, _gameManager);

        setCommandExecuter(commandExecuter);


        //_gameManager.SetSpawns();
//        _gameManager.redSpawn = defaultSpawn;
//        _gameManager.blueSpawn = defaultSpawn;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void setCommandExecuter(CommandExecuter commandExecuter) {
        for(String s : commandNames) {
            getCommand(s).setExecutor(commandExecuter);
        }
    }
}
