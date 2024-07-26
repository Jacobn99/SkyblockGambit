package org.jacobn99.skyblockgambit;

import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.CustomAdvancements.AdvancementManager;

public final class SkyblockGambit extends JavaPlugin {
    private String[] commandNames = {"start", "debug", "t", "set_spawn", "spawn_villager", "set_custom_item",
            "add_custom_item", "get_custom_item", "set_starter_chest", "end", "list_custom_items", "grant_advancement",
            "clear_custom_items"};
    GameManager _gameManager;
    EventManager _eventManager;
    CommandExecuter _commandExecuter;
    @Override
    public void onEnable() {
        _gameManager = new GameManager(this);
        _eventManager = new EventManager(this, _gameManager);
        //ConfigManager _configManager = new ConfigManager(this);
        saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(_eventManager, this);
        // Plugin startup logic
        _commandExecuter = new CommandExecuter(this, _gameManager);

        setCommandExecuter(_commandExecuter);
        _gameManager.InitializeTasks();



        //_gameManager.SetSpawns();
//        _gameManager.redSpawn = defaultSpawn;
//        _gameManager.blueSpawn = defaultSpawn;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        _gameManager.EndGame();
        _gameManager.LogEnabledTasks();


    }

    private void setCommandExecuter(CommandExecuter commandExecuter) {
        for(String s : commandNames) {
            getCommand(s).setExecutor(commandExecuter);
        }
    }
}
