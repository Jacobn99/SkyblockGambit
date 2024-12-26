package org.jacobn99.skyblockgambit.CustomWorlds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jacobn99.skyblockgambit.SerializedBlock;

import java.util.ArrayList;
import java.util.List;

public class NaturalCopier {
    WorldCopier _copier;
    public NaturalCopier(WorldCopier copier) {
        _copier = copier;
    }
    public void CloneLand(WorldManager manager, double oldX, double oldZ, double newX, double newZ) {
        int pieceLength = manager.get_pieceLength();
        int pieceHeight = manager.get_pieceHeight();
        int chunkWorldSize = manager.get_chunkWorldSize();

        long loopIterations = 0;
        for(int y = 0; y < (_copier.maxYLevel - _copier.minYLevel)/pieceHeight; y++) {
            for(int x = 0; x < chunkWorldSize; x++) {
                for(int z = 0; z < chunkWorldSize; z++) {
                    int xDistance = x * pieceLength;
                    int zDistance = z * pieceLength;
                    int yDistance = y * pieceHeight;

                    Location copyLoc = new Location(_copier._world,
                            (int)(oldX + xDistance), _copier.maxYLevel - yDistance, (int)(oldZ + zDistance));

                    Location pasteLoc = new Location(_copier._world,
                            (int)(newX + xDistance), _copier.maxYLevel - yDistance, (int)(newZ - zDistance));

                    _copier._processManager.CreateProcess(_copier.timeBetweenExecution * (loopIterations) + _copier._world.getFullTime(),
                            () -> ClonePiece(manager, copyLoc, pasteLoc));
                    loopIterations+=1;

                }
            }
        }
    }


    public void ClonePiece(WorldManager manager, Location copyLoc, Location pasteLoc) {
        List<SerializedBlock> data = CopyPiece(manager, copyLoc);
        PastePiece(data, pasteLoc);
    }
    public List<SerializedBlock> CopyPiece(WorldManager manager, Location topLeftLoc) {

        int pieceLength = manager.get_pieceLength();
        int pieceHeight = manager.get_pieceHeight();

        double referenceX = topLeftLoc.getX();
        double referenceY = topLeftLoc.getY();
        double referenceZ = topLeftLoc.getZ();

        List<SerializedBlock> dataList = new ArrayList<>();

        for (int y = 0; y < pieceHeight; y++) {
            for (int x = 0; x < pieceLength; x++) {
                for (int z = 0; z < pieceLength; z++) {
                    Block currentBlock = _copier._world.getBlockAt(
                            (int)(referenceX + x), ((int)referenceY - y), ((int)referenceZ - z));

                    //x, y, z are in reference to the location they were copied from (not exact coordinates)
                    dataList.add(new SerializedBlock(currentBlock.getBlockData(), x, y, z));
                }
            }
        }
        return dataList;
    }

    public void PastePiece(List<SerializedBlock> dataList, Location newLoc) {
        double referenceX = newLoc.getX();
        double referenceY = newLoc.getY();
        double referenceZ = newLoc.getZ();

        Bukkit.broadcastMessage("pasted");

        for(SerializedBlock block : dataList) {
            _copier._world.getBlockAt(
                    (int)(referenceX + block.get_x()),
                    (int)(referenceY - block.get_y()),
                    (int)(referenceZ + block.get_z()) ).setBlockData(block.get_data());
        }
    }

}
