package org.jacobn99.skyblockgambit.CustomWorlds;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.Processes.Process;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;
import org.jacobn99.skyblockgambit.SerializedBlock;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WorldCopier {
    JavaPlugin _mainPlugin;
    File _outputFile;
    List<Process> _processes;
    List<Process> _storedProcesses;
    ProcessManager _processManager;
    long timeBetweenExecution;
    int blocksGeneratedPerExecution;
    int maxYLevel;
    int minYLevel;
    World _world;

    public WorldCopier(JavaPlugin mainPlugin, List<Process> processes, ProcessManager processManager) {
        _mainPlugin = mainPlugin;
        _outputFile = new File(_mainPlugin.getDataFolder() + "/output.json");
        _processes = processes;
        _storedProcesses = new ArrayList<>();
        _processManager = processManager;

        timeBetweenExecution = 1; //in ticks
//        pieceHeight = ;
//        pieceLength = 16;
        blocksGeneratedPerExecution = 25000;
//        chunkWorldSize = 3;
        maxYLevel = 100;
        minYLevel = 0;
        _world = Bukkit.getWorld("void_world");
    }

    public void DuplicateLand(Location newLoc, int worldSize, File file) {
        long executionTime;
        int loopIterations;
        List<SerializedBlock> list;
        World world;
        newLoc.subtract((double) worldSize /2, 0 ,0);
        world = Bukkit.getWorld("void_world");
        list = GetChunkPieceData(file.getAbsolutePath());
        Queueable _queueable;
        loopIterations = 0;

        //Bukkit.broadcastMessage("list size: " + list.size());
        for (int i = 0; i < list.size(); i += blocksGeneratedPerExecution) {
            final int finalI = i;
            _queueable = () -> PasteChunkPiece(list, finalI, newLoc);
            executionTime = timeBetweenExecution * (loopIterations) + world.getFullTime();
//            executionTime = timeBetweenExecution * (loopIterations) + _processManager.GetLatestExecutionTime();

            Process process = new Process(executionTime, _queueable);
            _processManager.CreateProcess(process);
//            _storedProcesses.add(process);
//            Bukkit.broadcastMessage("putting " + executionTime);

            loopIterations++;
        }
//        for(Process process : _storedProcesses) {
//            _processManager.CreateProcess(process);
//        }
//        _processes.putAll(_storedProcesses);

//        long t = 0;
//        for(long time : _storedProcesses.keySet()) {
//            if(time > t) {
//                t = time;
//            }
//        }
//        Bukkit.broadcastMessage("latest execution real: " + t);
        //Bukkit.broadcastMessage("Process: " + _processes.size());
//        _storedProcesses.clear();
    }
    public void ClearWorld(Location referenceCorner, int worldSize) {
        Bukkit.broadcastMessage("Starting Deletion!");
        int loopIterations = 1;
        World world = Bukkit.getWorld("void_world");

        int chunkX = 50;
        int chunkY = 60;
        int chunkZ = 50;

        Location corner = referenceCorner.clone();
        corner.add(-(worldSize/2), -30, -(worldSize/2));

        for(int y = 0; y < 60; y+= chunkY) {
            for (int z = 0; z < worldSize; z += chunkZ) {
                for (int x = 0; x < worldSize; x+= chunkX) {
                    final int X = x;
                    final int Y = y;
                    final int Z = z;

//                    Process process = new Process(2 * (loopIterations) + world.getFullTime(),
//                            () -> ClearChunk(corner, X, Y, Z, chunkX, chunkY, chunkZ));
                    _processManager.CreateProcess(2 * (loopIterations) + world.getFullTime(),
                            () -> ClearChunk(corner, X, Y, Z, chunkX, chunkY, chunkZ));
                    loopIterations++;
                }
            }
        }

//        _processes.putAll(_storedProcesses);
//        _storedProcesses.clear();
    }
    public void ClearChunk(Location referenceCorner, int startX, int startY, int startZ, int chunkX, int chunkY, int chunkZ) {
        World world = Bukkit.getWorld("void_world");

        for(int y = startY; y < chunkY +startY; y+= 1) {
            for (int z = startZ; z < chunkZ + startZ; z += 1) {
                for (int x = startX; x < chunkX + startX; x+= 1) {
                    int X = x;
                    int Y = y;
                    int Z = z;
                    Location currentLoc = new Location(world, X + referenceCorner.getX(), Y+ referenceCorner.getY(), Z + referenceCorner.getZ());
                    if(currentLoc.getBlock().getType() != Material.AIR) {
                        currentLoc.getBlock().setType(Material.AIR);
                    }
                    currentLoc = null;
                }
            }
        }
    }
    public void PasteChunkPiece(List<SerializedBlock> list, int startIteration, Location newLoc) {
//        try {
            World world = Bukkit.getWorld("void_world");
            double xDistance = newLoc.getX() - list.get(0).get_x();
            double yDistance = newLoc.getY() - list.get(0).get_y();
            double zDistance = newLoc.getZ() - list.get(0).get_z();

            for (int i = startIteration; i < blocksGeneratedPerExecution + startIteration; i++) {
                if (i < list.size() - 1) {
                    SerializedBlock b = list.get(i);
                    Location blockLoc = new Location(world, b.get_x() + xDistance,
                            b.get_y() + yDistance, b.get_z() + zDistance);

                    BlockData newData = Bukkit.createBlockData(b.get_str_data());
                    blockLoc.getBlock().setBlockData(newData);

                    blockLoc = null;
                    newData = null;
                } else {
                    return;
                }
            }
//        } catch (Exception e) {
//            Bukkit.broadcastMessage("ERROR");
//            e.printStackTrace();
//            return;
//        }
    }
    public List<SerializedBlock> GetChunkPieceData(String filePath) {
        Gson gson = new Gson();
        try {
            Type listOfMyClassObject = new TypeToken<ArrayList<SerializedBlock>>() {
            }.getType();
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            List<SerializedBlock> blocksList = gson.fromJson(reader, listOfMyClassObject);
            return blocksList;
        } catch (IOException e) {
            Bukkit.broadcastMessage("ERROR AT PIECE DATA");
            throw new RuntimeException(e);
        }
    }
}