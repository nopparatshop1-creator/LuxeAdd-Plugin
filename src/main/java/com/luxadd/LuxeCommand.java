package com.luxadd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class LuxeCommand implements CommandExecutor {

    public static final String GUI_TITLE = ChatColor.DARK_PURPLE + "LuxeAdd - Custom Items";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        if (!player.hasPermission("luxeadd.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        openLuxeMenu(player);
        return true;
    }

    public static void openLuxeMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, GUI_TITLE);

        ItemStack customItem = createCustomModelItem(
                Material.PAPER, 
                ChatColor.GOLD + "Sword 3D Model", 
                10001, 
                ChatColor.GRAY + "Click to get this 3D Item"
        );

        gui.setItem(13, customItem);
        player.openInventory(gui);
    }

    public static ItemStack createCustomModelItem(Material material, String name, int modelData, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setCustomModelData(modelData);
            meta.setLore(Collections.singletonList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}
