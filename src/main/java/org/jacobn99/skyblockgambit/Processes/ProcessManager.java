package org.jacobn99.skyblockgambit.Processes;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jacobn99.skyblockgambit.GameManager;

import java.util.*;

public class ProcessManager {
    List<HashMap<Long, Process>> processGroups;
    boolean _taskDone = false;

    public ProcessManager() {
        processGroups = new ArrayList<>();
        _taskDone = false;
    }

    public void HandleProcesses(HashMap<Long, Process> processes) {

        World world = Bukkit.getWorld("void_world");
        List<Process> markedProcesses = new ArrayList<>();

        if(processes.size() > 0) {
            System.out.println("Processes size: " + processes.size());
        }

        List<Long> executionTimes = new ArrayList<>();
        executionTimes.addAll(processes.keySet());

        for(int i = 0; i < executionTimes.size(); i++) {
            long currentKey = executionTimes.get(i);
            Process process = processes.get(currentKey);

            if (world.getFullTime() >= currentKey && !this._taskDone) {
                this._taskDone = true;
                markedProcesses.add(process);
                process.set_isDone(true);
                try {
                    process.ExecuteFunction();
                }
                catch(Exception e) {
                    Bukkit.broadcastMessage("ERROR IN EVENT HANDLER");
                    e.printStackTrace();
                    markedProcesses.add(process);
                }
            }
        }

        for(Process process : markedProcesses) {
            processes.remove(process.get_executionTime());
            process = null;
        }
        markedProcesses.clear();
        this._taskDone = false;
    }
    public void CreateProcess(HashMap<Long, Process> processes, long executionTime, Queueable queueable) {
        long worldTime = Bukkit.getWorld("void_world").getFullTime();
        Process process = new Process(executionTime, queueable);
        if(worldTime > executionTime) {
            Bukkit.broadcastMessage("Warning: executionTime < world time");
        }
        processes.put(executionTime, process);
    }
    public long GetLatestExecutionTime(HashMap<Long, Process> processes) {
        long latestExecutionTime = 0;
        for(long executionTime : processes.keySet()) {
            if(executionTime > latestExecutionTime) {
                latestExecutionTime = executionTime;
            }
            //Bukkit.broadcastMessage("ex time: " + executionTime);
        }
        return latestExecutionTime;
    }

}

