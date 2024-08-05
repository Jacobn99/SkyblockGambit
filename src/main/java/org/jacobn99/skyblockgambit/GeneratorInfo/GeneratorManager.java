package org.jacobn99.skyblockgambit.GeneratorInfo;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GeneratorManager {
    public List<ItemGenerator> generators;
    public GeneratorManager() {
        generators = new ArrayList<>();
    }
    public ItemGenerator GetGeneratorType(Material material) {
        ItemGenerator generator = null;
        switch (material) {
            case DIAMOND:
                generator = new ItemGenerator(new ItemStack(Material.DIAMOND), 100, generators);
                generator.SetCost(new ItemStack(Material.DIAMOND, 5));
                break;
            case IRON_INGOT:
                generator = new ItemGenerator(new ItemStack(Material.IRON_INGOT), 100, generators);
                generator.SetCost(new ItemStack(Material.DIAMOND, 5));

                break;
            case REDSTONE:
                generator = new ItemGenerator(new ItemStack(Material.REDSTONE), 100, generators);
                generator.SetCost(new ItemStack(Material.DIAMOND, 5));
                break;
            default:
                break;
//            case :
//            case :
//            case :
        }
        return generator;
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
