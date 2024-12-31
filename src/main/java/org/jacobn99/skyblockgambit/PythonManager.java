package org.jacobn99.skyblockgambit;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.stream.Collectors;

import java.io.*;
import java.util.List;

public class PythonManager {
    JavaPlugin _mainPlugin;
    boolean isWindows;
    String path;
    public PythonManager(JavaPlugin mainPlugin) {
        _mainPlugin = mainPlugin;
        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        path = _mainPlugin.getDataFolder() + "\\Procedural-Terrain-Generator\\main.py";

    }
    public void GenerateNewIsland() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("python", path);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        List<String> results = convert(process.getInputStream());

//        int exitCode = process.waitFor();
    }
    private List<String> convert(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
}