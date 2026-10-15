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

        // หน่วงเวลา 1 วินาที ให้ผู้เล่นโหลดเข้าโลกเสร็จก่อนส่งคำขอ
        Bukkit.getScheduler().runTaskLater(LuxeAdd.getInstance(), () -> {
            if (player.isOnline()) {
                // ส่ง Title & Chat prompt
                if (config.getBoolean("resource-pack.welcome-message.enabled", true)) {
                    String title = ChatColor.translateAlternateColorCodes('&', 
                            config.getString("resource-pack.welcome-message.title", "&aResource Pack"));
                    String subtitle = ChatColor.translateAlternateColorCodes('&', 
                            config.getString("resource-pack.welcome-message.subtitle", "&fPlease accept the resource pack."));
                    player.sendTitle(title, subtitle, 10, 70, 20);

                    List<String> chatPrompt = config.getStringList("resource-pack.welcome-message.chat-prompt");
                    for (String line : chatPrompt) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                    }
                }

                // ส่งคำขอ Resource Pack ผ่านเมธอดใหม่
                LuxeCommand.sendResourcePack(player, config);
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
