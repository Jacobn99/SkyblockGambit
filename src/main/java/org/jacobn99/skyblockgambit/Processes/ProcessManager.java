package org.jacobn99.skyblockgambit.Processes;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jacobn99.skyblockgambit.GameManager;

import java.util.*;

public class ProcessManager {
    List<HashMap<Long, Process>> processGroups;
    boolean _taskDone = false;
    World _world;

    public ProcessManager() {
        processGroups = new ArrayList<>();
        _taskDone = false;
        _world = Bukkit.getWorld("void_world");
    }

    //Make processes not a sorted list
    public void AddToProcessesSorted(int element, List<Integer> list) {
        int lastIndex = list.size() - 1;
        if(list.size() > 0) {
            int index = FindSortedIndex(element, list);
//            Bukkit.broadcastMessage("old list: " + list);
//            Bukkit.broadcastMessage("element: " + index);
            list.add(index, element);
            Bukkit.broadcastMessage("new list: " + list);
        }
    }
    public int FindSortedIndex(int element, List<Integer> list) {
        return FindIndexHelper(element, list, 0);
    }
    public int FindIndexHelper(int element, List<Integer> list, int firstIndex) {
        if(list.size() == 1) {
//            Bukkit.broadcastMessage("index: " + firstIndex);
            return firstIndex + 1;
        }
        else if(list.size() == 2) {
            if(list.get(1) <= element)  {
                return firstIndex + 2;
            }
            else {
                return firstIndex + 1;
            }
        }
        else if(list.get(0) >= element) {
            return list.get(0);
        }
        else {

            int halfIndex = (int)((list.size() - 1)/2);
            int lastIndex = (list.size() - 1);
            List<Integer> list1 = list.subList(0, halfIndex + 1);
            List<Integer> list2 = list.subList(halfIndex + 1, lastIndex + 1); // Added (+1) because exclusive

            if(list2.get(0) < element) {
//                Bukkit.broadcastMessage("currentList: " + list2);
//                Bukkit.broadcastMessage("New First Index: " + firstIndex + " + " + halfIndex);
                return FindIndexHelper(element, list2, firstIndex + halfIndex + 1);
            }
            else if(list1.get(0) < element) {
//                Bukkit.broadcastMessage("New First Index: " + firstIndex);
//                Bukkit.broadcastMessage("currentList: " + list1);
                return FindIndexHelper(element,list1, firstIndex);
            }
            else {
//                assert(false);
                return -1;
            }
        }
//        return candidates;
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
//        Bukkit.broadcastMessage("putting " + executionTime);
    }

    public void CreateProcessLast(HashMap<Long, Process> processes, long delay, Queueable queueable) {
//        Bukkit.broadcastMessage("delay: " + (_world.getFullTime() + 10));
        CreateProcess(processes,_world.getFullTime() + 10, () -> CreateProcess(processes, GetLatestExecutionTime(processes) + delay, queueable));
    }
    public long GetLatestExecutionTime(HashMap<Long, Process> processes) {
        long latestExecutionTime = _world.getFullTime();
        for(long executionTime : processes.keySet()) {
            if(executionTime > latestExecutionTime) {
                latestExecutionTime = executionTime;
            }
//            Bukkit.broadcastMessage("ex time: " + executionTime);
        }
        Bukkit.broadcastMessage("latest execution time: " + latestExecutionTime);
        return latestExecutionTime;
    }

}

