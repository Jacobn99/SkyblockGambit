package org.jacobn99.skyblockgambit.Processes;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jacobn99.skyblockgambit.GameManager;

import java.util.*;

public class ProcessManager {
    World _world;
    GameManager _gameManager;

    public ProcessManager(GameManager gameManager) {
        _gameManager = gameManager;
        _world = Bukkit.getWorld("void_world");
    }

    //Make processes not a sorted list
    public void AddToProcessesSorted(Process newProcess) {
        List<Process> list = _gameManager.processes;
        if(list.size() > 0) {
            int index = FindSortedIndex(newProcess, list);
            list.add(index, newProcess);
        }
        else {
            list.add(newProcess);
        }
    }
    public int FindSortedIndex(Process newProcess, List<Process> list) {
        return FindIndexHelper(newProcess, list, 0);
    }
    public int FindIndexHelper(Process newProcess, List<Process> list, int firstIndex) {
        if(list.size() == 1) {
            return firstIndex + 1;
        }
        else if(list.size() == 2) {
            if(list.get(1).get_executionTime() <= newProcess.get_executionTime()) return firstIndex + 2;
            else return firstIndex + 1;
        }
        else if(list.get(0).get_executionTime() >= newProcess.get_executionTime()) {
            return firstIndex + 1;
        }
        else {
            int halfIndex = (int)((list.size() - 1)/2);
            int lastIndex = (list.size() - 1);
            List<Process> list1 = list.subList(0, halfIndex + 1);
            List<Process> list2 = list.subList(halfIndex + 1, lastIndex + 1); // Added (+1) because exclusive

            if(list2.get(0).get_executionTime() < newProcess.get_executionTime())
                return FindIndexHelper(newProcess, list2, firstIndex + halfIndex + 1);
            else if(list1.get(0).get_executionTime() < newProcess.get_executionTime())
                return FindIndexHelper(newProcess,list1, firstIndex);
            else return -1;
        }
    }


    public void HandleProcesses() {
        World world = Bukkit.getWorld("void_world");

        if(!_gameManager.processes.isEmpty()) {
            System.out.println("Processes size: " + _gameManager.processes.size());
            Process currentProcess = _gameManager.processes.get(0);
            if(currentProcess.get_executionTime() <= world.getFullTime()) {
                currentProcess.ExecuteFunction();
                _gameManager.processes.remove(0);
            }
        }
    }

    public void CreateProcess(long executionTime, Queueable queueable) {
        long worldTime = Bukkit.getWorld("void_world").getFullTime();

        Process process = new Process(executionTime, queueable);
        if(worldTime > executionTime) {
            Bukkit.broadcastMessage("Warning: executionTime < world time");
        }
        AddToProcessesSorted(process);

    }
    public void CreateProcess(Process process) {
        long worldTime = Bukkit.getWorld("void_world").getFullTime();
        long executionTime = process.get_executionTime();
        if(worldTime > executionTime) {
            Bukkit.broadcastMessage("Warning: executionTime < world time");
        }
        AddToProcessesSorted(process);
    }

    public long GetLatestExecutionTime() {
        long latestExecutionTime = _world.getFullTime();
        List<Process> processes = _gameManager.processes;

        if(!processes.isEmpty()) {
            latestExecutionTime = processes.get(processes.size() - 1).get_executionTime();
        }

        Bukkit.broadcastMessage("latest execution time: " + latestExecutionTime);
        return latestExecutionTime;
    }

}