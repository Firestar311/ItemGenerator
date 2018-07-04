package net.firecraftmc.itemgenerator;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Generator extends BukkitRunnable {

    private Block block;
    private ItemStack item;
    private double timeX;
    private double time;

    public Generator(Block block, ItemStack item, double time) {
        this.block = block;
        this.item = item;
        this.timeX = time;
        this.time = time;
    }

    public void run() {
        if (time <= 0) {
            block.getWorld().dropItem(block.getLocation().add(0.5, 1, 0.5), item);
            time = timeX;
        }
        time -= 0.05;
    }
}