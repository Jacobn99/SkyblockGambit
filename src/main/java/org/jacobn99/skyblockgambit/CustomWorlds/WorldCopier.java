package org.jacobn99.skyblockgambit.CustomWorlds;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.Processes.Process;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;
import org.jacobn99.skyblockgambit.SerializedBlock;

import java.io.*;
import java.lang.reflect.Executable;
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
    int pieceHeight;

    public WorldCopier(JavaPlugin mainPlugin, HashMap<Long, Process> processes, ProcessManager processManager) {
        _mainPlugin = mainPlugin;
        _outputFile = new File(_mainPlugin.getDataFolder() + "/output.json");
        _mainPlugin = mainPlugin;
        _processes = processes;
        _storedProcesses = new HashMap<>();
        _processManager = processManager;

        timeBetweenExecution = 1; //in ticks
        pieceHeight = 100;
        blocksGeneratedPerExecution = 25000;
        maxYLevel = 100;
        minYLevel = 0;
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

                    BlockData newData = Bukkit.createBlockData(b.get_data());
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