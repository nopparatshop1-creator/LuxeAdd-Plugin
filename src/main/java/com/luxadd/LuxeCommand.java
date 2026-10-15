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
        if (!sender.hasPermission("luxeadd.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        // หากพิมพ์ /luxeadd reload
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            LuxeAdd.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "[LuxeAdd] Configuration reloaded successfully!");
            return true;
        }

        // หากผู้เล่นพิมพ์ /luxeadd เฉยๆ ให้เปิด GUI
        if (sender instanceof Player player) {
            openLuxeMenu(player);
        } else {
            sender.sendMessage("Console Usage: /luxeadd reload");
        }

        return true;
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
