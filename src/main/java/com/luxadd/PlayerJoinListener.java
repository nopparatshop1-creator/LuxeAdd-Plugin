package com.luxadd;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = LuxeAdd.getInstance().getConfig();

        if (!config.getBoolean("resource-pack.enabled", false)) {
            return;
        }

        String url = config.getString("resource-pack.url", "");
        if (url.isEmpty() || url.contains("example.com")) {
            return;
        }

        String prompt = ChatColor.translateAlternateColorCodes('&', 
                config.getString("resource-pack.prompt-message", "&ePlease accept the resource pack."));
        
        boolean force = config.getBoolean("resource-pack.force", false);

        try {
            // ส่ง Resource Pack ให้ผู้เล่นแบบมาตรฐาน
            player.setResourcePack(url, null, prompt, force);
        } catch (Exception e) {
            LuxeAdd.getInstance().getLogger().warning("Could not send resource pack to " + player.getName() + ": " + e.getMessage());
        }
    }
}
