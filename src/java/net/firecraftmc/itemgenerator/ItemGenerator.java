package net.firecraftmc.itemgenerator;

import org.bukkit.plugin.java.JavaPlugin;

public class ItemGenerator extends JavaPlugin {

    public void onEnable() {
        this.getCommand("generator").setExecutor(new GeneratorManager(this));
    }
}