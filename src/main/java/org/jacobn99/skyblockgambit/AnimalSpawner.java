package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.jacobn99.skyblockgambit.CustomWorlds.CustomWorld;
import org.jacobn99.skyblockgambit.CustomWorlds.WorldManager;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class AnimalSpawner {
    GameManager _gameManager;
    WorldManager _worldManager;
    List<EntityType> _animalTypes;
    ProcessManager _processManager;
    World world;
    AnimalSpawner(GameManager gameManager, WorldManager worldManager, ProcessManager processManager) {
        _gameManager = gameManager;
        _worldManager = worldManager;
        _processManager = processManager;
        _animalTypes = new ArrayList<>();
        _animalTypes.add(EntityType.COW);
        _animalTypes.add(EntityType.PIG);
        _animalTypes.add(EntityType.CHICKEN);
        _animalTypes.add(EntityType.SHEEP);
        world = Bukkit.getWorld("void_world");

    }

    public void SpawnAnimals(boolean isTimed) {
        /*loop through all worlds
            Get world corner location

            if # of animals less than passive mob cap
                Make value (rand) a random value bound between 0 and the difference of animalCount and the passive mob cap
                Spawn rand animals at random locations
         */
        if (world.getFullTime() % 3600 == 0 || !isTimed) {
            Bukkit.broadcastMessage("what the sigma???");
//
            int animalCount = 0;
            int spawnTarget = 0;
            //Queueable queueable = () -> SpawnAnimals();


            Random rand = new Random();

            for (CustomWorld customWorld : _gameManager.customWorlds) {
                animalCount = GetAnimalPopulation(customWorld);
                if (animalCount < _gameManager.GetPassiveMobCap()) {
                    int difference = _gameManager.GetPassiveMobCap() - animalCount;
                    spawnTarget = rand.nextInt(difference);

                    for (int i = 0; i < spawnTarget; i++) {
                        Location animalLoc = _worldManager.GenerateSpawnLocation(Bukkit.getWorld("void_world"),
                                customWorld.GetMiddleLoc(), 300, _gameManager.minWorldHeight,
                                _worldManager.get_worldLength()/2);
                        Entity entity = animalLoc.getWorld().spawnEntity(animalLoc, RandomAnimalType());
                        entity.addScoreboardTag("disposable");
//                        entity.setGlowing(true);
                    }
                    Bukkit.broadcastMessage("Added: " + spawnTarget + " animals");
                }
            }
        }
    }
    private EntityType RandomAnimalType() {
        Random rand = new Random();

        return _animalTypes.get(rand.nextInt(_animalTypes.size()));

    }



    public int GetAnimalPopulation(CustomWorld customWorld) {
        int animalCount = 0;
        Location cornerLoc = customWorld.GetMiddleLoc().clone().subtract(0, 0, _worldManager.get_worldLength()/2);

        Collection<Entity> entities = Bukkit.getWorld("void_world").getNearbyEntities(cornerLoc, _worldManager.get_worldLength(), 70, _worldManager.get_worldLength());

        if(entities != null) {
            for (Entity e : entities) {
                if(_animalTypes.contains(e.getType())) {
                    animalCount++;
                }
            }
        }
        return animalCount;
    }

}
