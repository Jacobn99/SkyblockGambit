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
    HashMap<Long, Process> _processes;
    HashMap<Long, Process> _storedProcesses;
    ProcessManager _processManager;
    long timeBetweenExecution;
    int blocksGeneratedPerExecution;
    int maxYLevel;
    int minYLevel;
    World _world;

    public WorldCopier(JavaPlugin mainPlugin, HashMap<Long, Process> processes, ProcessManager processManager) {
        _mainPlugin = mainPlugin;
        _outputFile = new File(_mainPlugin.getDataFolder() + "/output.json");
        _processes = processes;
        _storedProcesses = new HashMap<>();
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

    public void DuplicateLand(Location newLoc, File file) {
        long executionTime;
        int loopIterations;
        List<SerializedBlock> list;
        World world;

        world = Bukkit.getWorld("void_world");
        list = GetChunkPieceData(file.getAbsolutePath());
        Queueable _queueable;
        loopIterations = 0;

        //Bukkit.broadcastMessage("list size: " + list.size());
        for (int i = 0; i < list.size(); i += blocksGeneratedPerExecution) {
            final int finalI = i;
            _queueable = () -> PasteChunkPiece(list, finalI, newLoc);
            executionTime = timeBetweenExecution * (loopIterations) + world.getFullTime();
            Process process = new Process(executionTime, _queueable);
            _storedProcesses.put(executionTime, process);
            loopIterations++;
        }
        _processes.putAll(_storedProcesses);
        //Bukkit.broadcastMessage("Process: " + _processes.size());
        _storedProcesses.clear();
    }
    public void ClearWorld(Location referenceCorner, int worldSize) {
        Bukkit.broadcastMessage("Starting Deletion!");
        Queueable queueable;
        int loopIterations = 1;
        long executionTime = 100;
        World world = Bukkit.getWorld("void_world");

        int startY = 0;
        int startX = 0;
        int startZ = 0;

        int chunkX = 25;
        int chunkY = 25;
        int chunkZ = 25;

        Location corner = referenceCorner.clone();
        corner.add(0, 0, -(worldSize/2));
        corner.add(0, -30, 0);

        for(int y = 0; y < 60; y+= chunkY) {
            for (int z = 0; z < worldSize; z += chunkZ) {
                for (int x = 0; x < worldSize; x+= chunkX) {
                    final int X = x;
                    final int Y = y;
                    final int Z = z;

                    _processManager.CreateProcess(_storedProcesses, 2 * (loopIterations) + world.getFullTime(), () -> ClearChunk(corner, X, Y, Z, chunkX, chunkY, chunkZ));
                    loopIterations++;
                }
            }
        }

        //Bukkit.broadcastMessage("size: " + _storedProcesses.size());
        _processes.putAll(_storedProcesses);
        _storedProcesses.clear();
    }
    public void ClearChunk(Location referenceCorner, int startX, int startY, int startZ, int chunkX, int chunkY, int chunkZ) {
        World world = Bukkit.getWorld("void_world");

        //Bukkit.broadcastMessage("X: " + startX + ", Z: " + startZ);

        for(int y = startY; y < chunkY +startY; y+= 1) {
            for (int z = startZ; z < chunkZ + startZ; z += 1) {
                for (int x = startX; x < chunkX + startX; x+= 1) {
//                    queueable = () -> Bukkit.broadcastMessage("Bruh");
//                    Process process = new Process(executionTime * loopIterations + world.getFullTime(), queueable);
//                    _processes.put(executionTime * loopIterations + world.getFullTime(), process);
                    int X = x;
                    int Y = y;
                    int Z = z;
                    Location currentLoc = new Location(world, X + referenceCorner.getX(), Y+ referenceCorner.getY(), Z + referenceCorner.getZ());
                    if(currentLoc.getBlock().getType() != Material.AIR) {
                        currentLoc.getBlock().setType(Material.AIR);
                    }
                    currentLoc = null;


                    //Bukkit.broadcastMessage("X1: " + x + ", Y1: " + Y + ", Z1: " + Z);
                    //_processManager.CreateProcess(_storedProcesses, timeBetweenExecution * (loopIterations) + world.getFullTime(), () -> ClearChunk(referenceCorner, X, Y, Z));

                    //_processManager.CreateProcess(_processes, 20, () -> Bukkit.broadcastMessage("Bruh"));
                }
            }
            //loopIterations++;
        }
//        World world = Bukkit.getWorld("void_world");
//       // Location currentLoc = referenceCorner.clone();
//        //Bukkit.broadcastMessage("x: " + Math.round((startX + referenceCorner.getX())) + ", y: " +  Math.round((startY + referenceCorner.getY())) + ", z: " +  Math.round((startZ + referenceCorner.getZ())));
//
//        //Bukkit.broadcastMessage("X2: " + startX + ", Y2: " + startY + ", Z2: " + startZ);
////        double Xloc;
////        double Yloc;
////        double Zloc;
//        double Xloc = referenceCorner.getX() + startX;
//        double Yloc = referenceCorner.getY();
//        double Zloc = referenceCorner.getZ() + startZ;
//
//        Bukkit.broadcastMessage("Xloc: " + Xloc + ", Yloc: " + Yloc + ", Zloc: " + Zloc);
//        //for(int y = startY; y < startY + 10; y++) {
////            for (int z = startZ; z < startZ + 10; z++) {
////                for (int x = startX; x <  startX + 100; x++) {
////                    Xloc = referenceCorner.getX() + x;
////                    Yloc = referenceCorner.getY();
////                    Zloc = referenceCorner.getZ() + z;
////                    Location currentLoc = new Location(world, Xloc, Yloc, Zloc);
////                    //currentLoc.add(1, 1, 0);
////                    currentLoc.getBlock().setType(Material.BLUE_CONCRETE);
////                    currentLoc = null;
////               }
////                //currentLoc.add(-5, 0, 0);
////            }
////            currentLoc.add(0,0,-worldSize);
//        //}
//        //System.gc();
    }
    public void PasteChunkPiece(List<SerializedBlock> list, int startIteration, Location newLoc) {
        try {
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

//                    if(i % 1000 == 1) {
//                        Bukkit.broadcastMessage("data: " + newData + ", Location: " + blockLoc);
//                    }

                    blockLoc = null;
                    newData = null;
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            Bukkit.broadcastMessage("ERROR");
            e.printStackTrace();
            return;
        }
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
            throw new RuntimeException(e);
        }
    }
}