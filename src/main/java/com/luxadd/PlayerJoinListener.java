package com.luxadd;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

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
        
        boolean force = config.getBoolean("resource-pack.force", true);

        try {
            // ใช้คำสั่ง setResourcePack แบบ 3 parameters สำหรับ Paper 1.21+
            player.setResourcePack(url, prompt, force);
        } catch (Exception e) {
            LuxeAdd.getInstance().getLogger().warning("Could not send resource pack to " + player.getName() + ": " + e.getMessage());
        }
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        FileConfiguration config = LuxeAdd.getInstance().getConfig();
        boolean force = config.getBoolean("resource-pack.force", true);

        if (!force) return;

        PlayerResourcePackStatusEvent.Status status = event.getStatus();

        if (status == PlayerResourcePackStatusEvent.Status.DECLINED || 
            status == PlayerResourcePackStatusEvent.Status.FAILED) {
            
            String kickMessage = ChatColor.translateAlternateColorCodes('&', 
                    config.getString("resource-pack.kick-message", "&cYou must accept the resource pack to play."));
            
            event.getPlayer().kickPlayer(kickMessage);
        }
    }
}
