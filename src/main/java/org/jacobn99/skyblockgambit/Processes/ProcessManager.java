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

        while(it.hasNext()) {
            Map.Entry item = (Map.Entry) it.next();
            long executionTime = (long) item.getKey();
            Process process = processes.get(executionTime);

            System.out.println("Processes size: " + processes.size());
            String command = "title @a actionbar {\"text\":\"Processes size: " + processes.size() + "\"}";
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            //Bukkit.broadcastMessage("isPreviousProcessDone: " + isPreviousProcessDone);36

            //Bukkit.broadcastMessage(world.getFullTime() + ", " + executionTime);
            //if(world.getFullTime() >= executionTime && isPreviousProcessDone) {
            if(world.getFullTime() >= executionTime) {

                //Bukkit.broadcastMessage(world.getFullTime() + ", " + executionTime);

                //Bukkit.broadcastMessage(world.getFullTime() + ", " + executionTime);
                if(process._isDone) {
                    it.remove();
                }
                else {
                    process.set_isDone(true);
                    process.ExecuteFunction();
                }
                //processes.remove(process);
            }
        }
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

