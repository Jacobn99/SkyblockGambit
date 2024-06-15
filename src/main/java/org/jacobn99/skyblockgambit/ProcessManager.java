package org.jacobn99.skyblockgambit;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProcessManager {
    public void HandleProcesses(HashMap<Long, Process> processes) {
        World world = Bukkit.getWorld("void_world");
        Iterator it = processes.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry item = (Map.Entry) it.next();
            long executionTime = (long) item.getKey();
            Process process = processes.get(executionTime);

            System.out.println("Processes size: " + processes.size());
            //Bukkit.broadcastMessage("isPreviousProcessDone: " + isPreviousProcessDone);

            //Bukkit.broadcastMessage(world.getFullTime() + ", " + executionTime);
            //if(world.getFullTime() >= executionTime && isPreviousProcessDone) {
            if(world.getFullTime() >= executionTime) {

                //Bukkit.broadcastMessage(world.getFullTime() + ", " + executionTime);

                //Bukkit.broadcastMessage(world.getFullTime() + ", " + executionTime);

                process.ExecuteFunction();
                it.remove();
            }
        }
    }

}

