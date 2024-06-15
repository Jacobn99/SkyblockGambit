package org.jacobn99.skyblockgambit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WorldCopier {
    JavaPlugin _mainPlugin;
    File _file;
    HashMap<Long, Process> _processes;
    HashMap<Long, Process> _storedProcesses;
    ProcessManager _processManager;
    long timeBetweenExecution;
    int maxGenerateHeight;
    //private boolean _canProceed;
//    boolean isCopying;
    int pieceHeight;
    public WorldCopier(JavaPlugin mainPlugin, HashMap<Long, Process> processes) {
        _mainPlugin = mainPlugin;
        _file = new File(_mainPlugin.getDataFolder() + "/bro.json");
        _mainPlugin = mainPlugin;
        pieceHeight = 40;
        _processes = processes;
        _storedProcesses = new HashMap<>();
        timeBetweenExecution = 1;
        maxGenerateHeight = 200;
        _processManager = new ProcessManager();
        //_canProceed = canProceed;
        //isCopying = false;
    }

    public void DuplicateLand(int XChunks, int ZChunks, Location generalStartLoc, Location newLoc) {
//        ArrayList<Long> keyList = new ArrayList();
//        ArrayList<Queueable> valueList = new ArrayList();
        Process lastProcess = null;

        int loopIterations = 0;
        for (int x = 0; x < XChunks; x++) {
            //int finalXVariable = x;
            Bukkit.broadcastMessage("x: " + x);
            World world = Bukkit.getWorld("void_world");
            for (int z = 0; z < ZChunks; z++) {
                long executionTime;
                List<SerializedBlock> currentData;
                Location currentCopiedChunkLoc = new Location(world, generalStartLoc.getX() + (x * 16), -64, generalStartLoc.getZ() + (z * 16));

                CopyChunkPieceData(_file.getAbsolutePath(), currentCopiedChunkLoc);
                currentData = GetChunkPieceData(_file.getAbsolutePath());
                for (int y = 0; y < maxGenerateHeight; y += pieceHeight) {
                    Location currentPasteLoc = new Location(world, newLoc.getX() + (x * 16), -64, newLoc.getZ() + (z * 16));
                    loopIterations++;
                    executionTime = timeBetweenExecution * (loopIterations) + world.getFullTime();

                    Queueable _queueable = () -> PasteChunkPiece(currentData, currentPasteLoc, pieceHeight);
                    Process process = new Process(executionTime, _queueable, lastProcess);
                    lastProcess = process;

                    _storedProcesses.put(executionTime, process);
                }
            }
        }
        _processes.putAll(_storedProcesses);
        _storedProcesses.clear();
    }

//    private void PasteChunk(int ZChunks, int x, Location generalStartLoc, Location newLoc) {
//        //Bukkit.broadcastMessage("GOT HERE");
//        World world = Bukkit.getWorld("void_world");
//        for (int z = 0; z < ZChunks; z++) {
//            long executionTime = 0;
//            List<SerializedBlock> currentData;
//            Location currentCopiedChunkLoc = new Location(world, generalStartLoc.getX() + (x * 16), -64, generalStartLoc.getZ() + (z * 16));
//
//            CopyChunkPieceData(_file.getAbsolutePath(), currentCopiedChunkLoc);
//            currentData = GetChunkPieceData(_file.getAbsolutePath());
//            for (int y = 0; y < 256; y += pieceHeight) {
//                executionTime = 2L * (y / pieceHeight);
//                Queueable _queueable = () -> PasteChunkPiece(currentData, newLoc, pieceHeight);
//                _processes.put(world.getFullTime() + executionTime, _queueable);
//                Bukkit.broadcastMessage("Processes size: " + _processes.size());
//            }
//        }
//    }
    public void PasteChunkPiece(List<SerializedBlock> list, Location newLoc, int pieceHeight) {
        try {
            //Bukkit.broadcastMessage("Processes size: " + _processes.size());

            World world = Bukkit.getWorld("void_world");
//        Location loc = new Location(world, -61, 71, 127);
//        CopyChunkData(_mainPlugin.getDataFolder() + "/bro.json", loc);
//        List<SerializedBlock> list = GetSerializedBlockData(_mainPlugin.getDataFolder() + "/bro.json")
            double xDistance = newLoc.getX() - list.get(0).get_x();
            double ZDistance = newLoc.getZ() - list.get(0).get_z();


            for (int i = 0; i < pieceHeight * 16 * 16; i++) {
                if (!list.isEmpty()) {
                    SerializedBlock b = list.get(0);
                    Location blockLoc = new Location(world, b.get_x() + xDistance, b.get_y(), b.get_z() + ZDistance);
                    //Bukkit.broadcastMessage("x: " + b.get_x() + xDistance + ", y: " + b.get_y() + ", z: " + b.get_z() + ZDistance);
                    BlockData newData = Bukkit.createBlockData(b.get_data());
                    blockLoc.getBlock().setBlockData(newData);
                    list.remove(b);

                    blockLoc = null;
                    newData = null;
                } else {
                    Bukkit.broadcastMessage("too big");
                    return;
                }
//                if (i < list.size()) {
//                    SerializedBlock b = list.get(i);
//                    Location blockLoc = new Location(world, b.get_x() + xDistance, b.get_y(), b.get_z() + ZDistance);
//                    //Bukkit.broadcastMessage("x: " + b.get_x() + xDistance + ", y: " + b.get_y() + ", z: " + b.get_z() + ZDistance);
//                    BlockData newData = Bukkit.createBlockData(b.get_data());
//                    blockLoc.getBlock().setBlockData(newData);
//                    list.remove(b);
//
//                    blockLoc = null;
//                    newData = null;
//                } else {
//                    Bukkit.broadcastMessage("too big");
//                    return;
//                }
            }
        }catch (Exception e) {
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

        //Bukkit.broadcastMessage(cornerLoc.toString());
        Gson gson = new Gson();

        try {
            Writer writer = Files.newBufferedWriter(Paths.get(filePath));
            List<SerializedBlock> serializedBlocks = new ArrayList<>();
            for (int y = 0; y < maxGenerateHeight; y++) {
                //Bukkit.broadcastMessage("y: " + y);
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
    private List<SerializedBlock> GetChunkPieceData(String filePath) {
        Gson gson = new Gson();
        try {
            Type listOfMyClassObject = new TypeToken<ArrayList<SerializedBlock>>() {}.getType();
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            List<SerializedBlock> blocksList = gson.fromJson(reader, listOfMyClassObject);
            return blocksList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}