package org.jacobn99.skyblockgambit.GeneratorInfo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GeneratorManager {
    public List<ItemGenerator> generators;
    public int maxGenerators;
    public GeneratorManager() {
        generators = new ArrayList<>();
        maxGenerators = 4;
    }

    public ItemGenerator GetGeneratorType(Material material) {
        ItemGenerator generator = null;
        switch (material) {
            case DIAMOND:
                generator = new ItemGenerator(new ItemStack(Material.DIAMOND), 1600, generators);
                generator.SetCost(new ItemStack(Material.DIAMOND, 5));
                break;
            case IRON_INGOT:
                generator = new ItemGenerator(new ItemStack(Material.IRON_INGOT), 300, generators);
                generator.SetCost(new ItemStack(Material.IRON_INGOT, 5));
                break;
            case REDSTONE:
                generator = new ItemGenerator(new ItemStack(Material.REDSTONE), 100, generators);
                generator.SetCost(new ItemStack(Material.REDSTONE_BLOCK, 1));
                break;
            case EMERALD:
                generator = new ItemGenerator(new ItemStack(Material.EMERALD), 500, generators);
                generator.SetCost(new ItemStack(Material.EMERALD, 7));
                break;
            default:
                break;
//            case :
//            case :
//            case :
        }
        return generator;
    }
    public void RenewGenerators(long tickRate) {
        for(ItemGenerator g : GetGenerators()) {
            if (g.GetGenerateTimeRemaining() <= 0) {
                if(g.GetGenerateDelay() <= 0) Bukkit.broadcastMessage("generate delay: "  + g.GetGenerateDelay());
                g.AddGenerateTime(g.GetGenerateDelay());
                g.Generate();
            }
            g.AddGenerateTime(-tickRate);
            if(g.GetClockDisplay() != null) {
                g.GetClockDisplay().setCustomName("Seconds remaining: " + g._generateTimeRemaining / 20);
            }
        }
    }
//    private void InitializeGeneratorTemplates() {
//    }

//    public List<ItemGenerator> GetGeneratorTemplates() {
//        return _generatorTemplates;
//    }

    public List<ItemGenerator> GetGenerators() {
        return generators;
    }
}
