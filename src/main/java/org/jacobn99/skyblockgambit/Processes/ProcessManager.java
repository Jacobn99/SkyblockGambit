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
        //Iterator it = processes.entrySet().iterator();
        List<Process> markedProcesses = new ArrayList<>();

        if(processes.size() > 0) {
            System.out.println("Processes size: " + processes.size());
        }

        List<Long> executionTimes = new ArrayList<>();
        executionTimes.addAll(processes.keySet());

        for(int i = 0; i < executionTimes.size(); i++) {
            long currentKey = executionTimes.get(i);
            Process process = processes.get(currentKey);

            if (world.getFullTime() >= currentKey) {
                if(!process._isDone) {
                    markedProcesses.add(process);
                    process.set_isDone(true);
                    process.ExecuteFunction();
                }
            }
        }
//        while(it.hasNext()) {
//            //try {
//            Map.Entry item = (Map.Entry) it.next();
//            long executionTime = (long) item.getKey();
//            Process process = processes.get(executionTime);
//
//            if (world.getFullTime() >= executionTime) {
//                if(!process._isDone) {
//                    markedProcesses.add(process);
//                    process.set_isDone(true);
//                    process.ExecuteFunction();
//                }
//            }
//        }

        for(Process process : markedProcesses) {
            processes.remove(process.get_executionTime());
            process = null;
            //previousProcess = null;
        }
        markedProcesses.clear();
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

