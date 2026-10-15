package com.luxadd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LuxeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        FileConfiguration config = LuxeAdd.getInstance().getConfig();

        // คำสั่ง /luxeadd pack
        if (args.length > 0 && args[0].equalsIgnoreCase("pack")) {
            if (sender instanceof Player player) {
                sendResourcePack(player, config);
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
            return true;
        }

        // คำสั่ง /luxeadd reload
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("luxeadd.admin")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }
            LuxeAdd.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "[LuxeAdd] Configuration reloaded successfully!");
            return true;
        }

        // คำสั่ง /luxeadd (เปิด GUI)
        if (sender instanceof Player player) {
            openLuxeMenu(player);
        } else {
            sender.sendMessage("Console Usage: /luxeadd reload");
        }

        return true;
    }

    public static void sendResourcePack(Player player, FileConfiguration config) {
        String url = config.getString("resource-pack.url", "");
        if (url.isEmpty() || url.contains("example.com")) {
            player.sendMessage(ChatColor.RED + "Resource pack URL is not properly configured!");
            return;
        }

        String promptText = config.getString("resource-pack.prompt-message", "&eDo you want to download the server resource pack?");
        promptText = ChatColor.translateAlternateColorCodes('&', promptText);
        boolean force = config.getBoolean("resource-pack.force", false);

        try {
            // ส่งแบบระบุ Hash เป็นค่าว่าง "" เพื่อป้องกันไม่ให้ Paper นำ Prompt ไปคิดว่าเป็น Hash
            player.setResourcePack(url, "", promptText, force);
            player.sendMessage(ChatColor.GREEN + "Sending resource pack request...");
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error sending resource pack: " + e.getMessage());
        }
    }

    public static void openLuxeMenu(Player player) {
        FileConfiguration config = LuxeAdd.getInstance().getConfig();
        
        String title = ChatColor.translateAlternateColorCodes('&', 
                config.getString("gui.title", "&5LuxeAdd - Custom Items"));
        
        Inventory gui = Bukkit.createInventory(null, 27, title);

        String matName = config.getString("items.sword_3d.material", "PAPER");
        Material material = Material.matchMaterial(matName);
        if (material == null) material = Material.PAPER;

        int modelData = config.getInt("items.sword_3d.model-data", 10001);
        int slot = config.getInt("items.sword_3d.slot", 13);
        
        String name = ChatColor.translateAlternateColorCodes('&', 
                config.getString("items.sword_3d.display-name", "&6Sword 3D Model"));
        
        List<String> rawLore = config.getStringList("items.sword_3d.lore");
        List<String> lore = new ArrayList<>();
        for (String line : rawLore) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        ItemStack customItem = createCustomModelItem(material, name, modelData, lore);
        gui.setItem(slot, customItem);

        player.openInventory(gui);
    }

    public static ItemStack createCustomModelItem(Material material, String name, int modelData, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setCustomModelData(modelData);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}
