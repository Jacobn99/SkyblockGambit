package org.jacobn99.skyblockgambit.Processes;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jacobn99.skyblockgambit.GameManager;

import java.util.*;

public class ProcessManager {
    List<HashMap<Long, Process>> processGroups;

    public ProcessManager() {
        processGroups = new ArrayList<>();
    }

    public void HandleProcesses(HashMap<Long, Process> processes) {
        World world = Bukkit.getWorld("void_world");
        Iterator it = processes.entrySet().iterator();
        List<Process> markedProcesses = new ArrayList<>();

        while(it.hasNext()) {
            //try {
            Map.Entry item = (Map.Entry) it.next();
            long executionTime = (long) item.getKey();
            Process process = processes.get(executionTime);

            System.out.println("Processes size: " + processes.size());
            if (world.getFullTime() >= executionTime) {
                if(!process._isDone) {
                    markedProcesses.add(process);
                    process.set_isDone(true);
                    process.ExecuteFunction();
                }
            }
        }

        for(Process process : markedProcesses) {
            processes.remove(process.get_executionTime());
        }
        markedProcesses.clear();
    }
    public void CreateProcess(HashMap<Long, Process> processes, long executionTime, Queueable queueable) {
        Bukkit.broadcastMessage("IT WAS called");

        Process process = new Process(executionTime, queueable);
        processes.put(executionTime, process);
    }
    public long GetLatestExecutionTime(HashMap<Long, Process> processes) {
        long latestExecutionTime = Bukkit.getWorld("void_world").getFullTime();
        for(long executionTime : processes.keySet()) {
            if(executionTime > latestExecutionTime) {
                latestExecutionTime = executionTime;
            }
            //Bukkit.broadcastMessage("ex time: " + executionTime);
        }
        return latestExecutionTime;
    }

}

