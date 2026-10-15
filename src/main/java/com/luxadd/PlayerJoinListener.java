package com.luxadd;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.nio.charset.StandardCharsets;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = LuxeAdd.getInstance().getConfig();

        if (!config.getBoolean("resource-pack.enabled", false)) {
            return;
        }

        String url = config.getString("resource-pack.url", "");
        if (url.isEmpty() || url.equals("https://example.com/your-resource-pack.zip")) {
            return;
        }

        String hashString = config.getString("resource-pack.hash", "");
        byte[] hash = hashString.isEmpty() ? null : hashString.getBytes(StandardCharsets.UTF_8);

        String prompt = ChatColor.translateAlternateColorCodes('&', 
                config.getString("resource-pack.prompt-message", "&ePlease accept the resource pack."));
        
        boolean force = config.getBoolean("resource-pack.force", false);

        // ส่ง Resource Pack ให้ผู้เล่น (รองรับ API Minecraft 1.21+)
        try {
            if (hash != null && hash.length > 0) {
                player.setResourcePack(url, hash, prompt, force);
            } else {
                player.setResourcePack(url, prompt, force);
            }
        } catch (Exception e) {
            LuxeAdd.getInstance().getLogger().warning("Could not send resource pack to " + player.getName() + ": " + e.getMessage());
        }
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        FileConfiguration config = LuxeAdd.getInstance().getConfig();
        boolean force = config.getBoolean("resource-pack.force", false);

        if (!force) return;

        // หากตั้งค่า force: true แล้วผู้เล่นปฏิเสธ (DECLINED) หรือดาวน์โหลดล้มเหลว (FAILED)
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || 
            event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED) {
            
            String kickMessage = ChatColor.translateAlternateColorCodes('&', 
                    config.getString("resource-pack.kick-message", "&cYou must accept the resource pack."));
            
            event.getPlayer().kickPlayer(kickMessage);
        }
    }
}
