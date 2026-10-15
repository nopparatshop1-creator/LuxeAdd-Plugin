package com.luxadd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.List;

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
            LuxeAdd.getInstance().getLogger().warning("Resource pack URL is invalid or set to default example.com!");
            return;
        }

        String prompt = ChatColor.translateAlternateColorCodes('&', 
                config.getString("resource-pack.prompt-message", "&eDo you want to download the server resource pack?"));
        
        boolean force = config.getBoolean("resource-pack.force", false);

        // หน่วงเวลา 20 Ticks (1 วินาที) ก่อนส่งคำถามให้ผู้เล่น
        Bukkit.getScheduler().runTaskLater(LuxeAdd.getInstance(), () -> {
            if (player.isOnline()) {
                // ส่ง Title & Subtitle ถามผู้เล่น
                if (config.getBoolean("resource-pack.welcome-message.enabled", true)) {
                    String title = ChatColor.translateAlternateColorCodes('&', 
                            config.getString("resource-pack.welcome-message.title", "&aResource Pack"));
                    String subtitle = ChatColor.translateAlternateColorCodes('&', 
                            config.getString("resource-pack.welcome-message.subtitle", "&fPlease accept the resource pack."));
                    player.sendTitle(title, subtitle, 10, 70, 20);

                    // ส่งข้อความแจ้งเตือนทางช่องแชท
                    List<String> chatPrompt = config.getStringList("resource-pack.welcome-message.chat-prompt");
                    for (String line : chatPrompt) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                    }
                }

                // แสดงกล่องป๊อปอัพถามผู้เล่นตามระบบมาตรฐานของ Minecraft
                try {
                    player.setResourcePack(url, prompt, force);
                } catch (Exception e) {
                    LuxeAdd.getInstance().getLogger().warning("Could not send resource pack to " + player.getName() + ": " + e.getMessage());
                }
            }
        }, 20L);
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        FileConfiguration config = LuxeAdd.getInstance().getConfig();
        boolean force = config.getBoolean("resource-pack.force", false);

        if (!force) return;

        PlayerResourcePackStatusEvent.Status status = event.getStatus();

        if (status == PlayerResourcePackStatusEvent.Status.DECLINED || 
            status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            
            String kickMessage = ChatColor.translateAlternateColorCodes('&', 
                    config.getString("resource-pack.kick-message", "&cYou must accept the resource pack to play."));
            
            event.getPlayer().kickPlayer(kickMessage);
        }
    }
}
