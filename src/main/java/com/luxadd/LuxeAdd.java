package com.luxadd;

import org.bukkit.plugin.java.JavaPlugin;

public class LuxeAdd extends JavaPlugin {

    private static LuxeAdd instance;

    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        
        if (getCommand("luxeadd") != null) {
            getCommand("luxeadd").setExecutor(new LuxeCommand());
        }
        
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        getLogger().info("LuxeAdd Plugin enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("LuxeAdd Plugin disabled.");
    }

    public static LuxeAdd getInstance() {
        return instance;
    }
}
