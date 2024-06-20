package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.Portals.Portal;
import org.jacobn99.skyblockgambit.Portals.PortalManager;
import org.jacobn99.skyblockgambit.Processes.Process;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WorldManager {
    private Random rand;
    private List<CustomWorld> _customWorlds;
    private WorldCopier _worldCopier;
    private JavaPlugin _mainPlugin;
    private int _worldLength;
    private GameManager _gameManager;
    private PortalManager _portalManager;
    //private int _minWorldHeight;

    WorldManager(JavaPlugin mainPlugin, GameManager gameManager, PortalManager portalManager, List<CustomWorld> customWorlds) {
        rand = new Random();
        _mainPlugin = mainPlugin;
        _worldLength = 300;

        _gameManager = gameManager;
        _portalManager = portalManager;
        _worldCopier = new WorldCopier(_mainPlugin, _gameManager.processes);
        _customWorlds = customWorlds;
        //_minWorldHeight = 94;
    }
    public Location FindRandomWorldSpawn(CustomWorld world) {
        int x;
        int z;
        int sideLength;
        Location referenceCorner;
        //final i chunkLength = _worldChunkLength;

        referenceCorner = world.GetReferenceCorner();
        sideLength = _worldLength;

        for(int i = 0; i < 20; i++) {
            x = rand.nextInt(sideLength);
            z = rand.nextInt(sideLength);
            //x = rand.nextDouble(50.0) - (sideLength/2.0);
            //z = rand.nextDouble(50.0) - (sideLength/2.0);

            Location loc = new Location(Bukkit.getWorld("void_world"), referenceCorner.getX() + x, referenceCorner.getY(), referenceCorner.getZ() + z);
            loc = _gameManager.FindSurface(loc, 300, _gameManager.minWorldHeight);

            if(loc != null) {
                return loc;
            }
        }
        return referenceCorner;
    }

    public void SpawnPortals() {
        Location portalLoc;
        //CustomWorld currentWorld;
        CustomWorld currentOpposingWorld;

        int i = 0;
        for(CustomWorld customWorld : _customWorlds) {
            portalLoc = _gameManager.FindSurface(customWorld.GetWorldSpawn(), 300, _gameManager.minWorldHeight);
            //currentWorlds.remove(customWorld);
            //currentWorld = customWorld;

            if(i < _customWorlds.size() - 1) {
                currentOpposingWorld = _customWorlds.get(i + 1);
            }
            else if (_customWorlds.size() > 1) {
                currentOpposingWorld = _customWorlds.get(i - 1);
            }
            else {
                Bukkit.broadcastMessage("ERROR: NO CUSTOM WORLD");
                return;
            }
            Bukkit.broadcastMessage("got here");

            Portal p = new Portal(_gameManager.portals, _portalManager, currentOpposingWorld.GetWorldSpawn(), portalLoc);
            p.Activate();

            //currentWorlds.add(customWorld);
            i++;
        }
    }
    public void BuildWorld(CustomWorld newWorld, File worldFile) {
        ProcessManager _processManager = new ProcessManager();
        File file = new File( _mainPlugin.getDataFolder().getAbsolutePath() + "/output.json");
//        long exececutionTime = _processManager.GetLatestExecutionTime(_gameManager.processes) + 5;
//        _worldCopier.DuplicateLand(new Location(Bukkit.getWorld("void_world"), -378, 96, 417),
//                file);
        _worldCopier.DuplicateLand(newWorld.GetReferenceCorner(), worldFile);
//        Queueable queueable = this::SpawnPortals;
//        Process process = new Process(exececutionTime, queueable);
//        _gameManager.processes.put(exececutionTime, process);

        //_processManager.GetLatestExecutionTime(_gameManager.processes);

        //_gameManager.processes.
    }
//    public void PasteSchematic() {
//        Clipboard clipboard;
//        ClipboardFormat format = ClipboardFormats.findByFile(file);
//        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
//            clipboard = reader.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
//            Operation operation = new ClipboardHolder(clipboard)
//                    .createPaste(editSession)
//                    .to(BlockVector3.at(-147, 78, 106))
//                    .ignoreAirBlocks(false)
//                    .build();
//            Operations.complete(operation);
//        } catch (WorldEditException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
