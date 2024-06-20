package org.jacobn99.skyblockgambit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.jacobn99.skyblockgambit.Processes.Process;
import org.jacobn99.skyblockgambit.Processes.ProcessManager;
import org.jacobn99.skyblockgambit.Processes.Queueable;
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
    //private boolean _canProceed;
//    boolean isCopying;
    int pieceHeight;

    public WorldCopier(JavaPlugin mainPlugin, HashMap<Long, Process> processes) {
        _mainPlugin = mainPlugin;
        _outputFile = new File(_mainPlugin.getDataFolder() + "/output.json");
        _mainPlugin = mainPlugin;
        _processes = processes;
        _storedProcesses = new HashMap<>();
        _processManager = new ProcessManager();

        timeBetweenExecution = 1; //in ticks
        pieceHeight = 100;
        blocksGeneratedPerExecution = 25000;
        maxYLevel = 100;
        minYLevel = 0;
    }

    public void DuplicateLand(/*int XChunks, int ZChunks, Location generalStartLoc,*/ Location newLoc, File file) {
        long executionTime;
        int loopIterations;
        List<SerializedBlock> list;
        World world;

        world = Bukkit.getWorld("void_world");
        list = GetChunkPieceData(file.getAbsolutePath());
        Queueable _queueable;
        loopIterations = 0;

        //while(!list.isEmpty()) {
        Bukkit.broadcastMessage("list size: " + list.size());
        for(int i = 0; i < list.size(); i += 25000) {
            final int finalI = i;
            _queueable = () -> PasteChunkPiece(list, finalI, newLoc, pieceHeight);
            executionTime = timeBetweenExecution * (loopIterations) + world.getFullTime();
            Process process = new Process(executionTime, _queueable);
            _storedProcesses.put(executionTime, process);
            loopIterations++;
        }
        _processes.putAll(_storedProcesses);
        Bukkit.broadcastMessage("Process: " + _processes.size());
        _storedProcesses.clear();
    }

    public void PasteChunkPiece(List<SerializedBlock> list, int startIteration, Location newLoc, int pieceHeight) {
        try {
            World world = Bukkit.getWorld("void_world");
            double xDistance = newLoc.getX() - list.get(0).get_x();
            double yDistance = newLoc.getY() - list.get(0).get_y();
            double zDistance = newLoc.getZ() - list.get(0).get_z();


            Bukkit.broadcastMessage("startIteration: " + startIteration);
            for (int i = startIteration; i < blocksGeneratedPerExecution + startIteration; i++) {
            //for (int i = startIteration; i < 200 + startIteration; i++) {
                if (i < list.size() - 1) {
                    SerializedBlock b = list.get(i);
                    Location blockLoc = new Location(world, b.get_x() + xDistance,
                            b.get_y() + yDistance, b.get_z() + zDistance);

                    BlockData newData = Bukkit.createBlockData(b.get_data());
                    //Bukkit.broadcastMessage("data: " + newData + "Location: " + newLoc);
                    blockLoc.getBlock().setBlockData(newData);

//                    if(blockLoc.getBlock().getType() != Material.NETHER_PORTAL) {
//                        blockLoc.getBlock().setBlockData(newData);
//                    }
                    //list.remove(b);

                    blockLoc = null;
                    newData = null;
                } else {
                    //Bukkit.broadcastMessage("too big");
                    return;
                }
            }
//            for (int i = 0; i < pieceHeight * 16 * 16; i++) {
//                if (!list.isEmpty()) {
//                    SerializedBlock b = list.get(0);
//                    Location blockLoc = new Location(world, b.get_x() + xDistance,
//                            b.get_y() + yDistance, b.get_z() + zDistance);
//
//                    BlockData newData = Bukkit.createBlockData(b.get_data());
//                    blockLoc.getBlock().setBlockData(newData);
//
////                    if(blockLoc.getBlock().getType() != Material.NETHER_PORTAL) {
////                        blockLoc.getBlock().setBlockData(newData);
////                    }
//                    list.remove(b);
//
//                    blockLoc = null;
//                    newData = null;
//                } else {
//                    //Bukkit.broadcastMessage("too big");
//                    return;
//                }
//            }
        } catch (Exception e) {
            Bukkit.broadcastMessage("ERROR");
            e.printStackTrace();
            return;
        }
    }

    private void CopyChunkPieceData(String filePath, Location cornerLoc) {
        World world = Bukkit.getWorld("void_world");
        double cornerX = cornerLoc.getX() - 1;
        double cornerY = cornerLoc.getY();
        double cornerZ = cornerLoc.getZ();

        Gson gson = new Gson();

        try {
            Writer writer = Files.newBufferedWriter(Paths.get(filePath));
            List<SerializedBlock> serializedBlocks = new ArrayList<>();
            for (int y = minYLevel; y < maxYLevel; y++) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        Location currentLoc = new Location(world, cornerX - x, cornerY + y, cornerZ - z);
                        SerializedBlock serializedBlock = new SerializedBlock(currentLoc.getBlock().getBlockData().getAsString(), currentLoc.getX(), currentLoc.getY(), currentLoc.getZ());
                        serializedBlocks.add(serializedBlock);
                        currentLoc = null;
                    }
                }
            }
            gson.toJson(serializedBlocks, writer);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gson = null;
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