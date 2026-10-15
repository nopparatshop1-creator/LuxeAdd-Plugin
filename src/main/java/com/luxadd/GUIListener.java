package com.luxadd;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        FileConfiguration config = LuxeAdd.getInstance().getConfig();
        String expectedTitle = ChatColor.translateAlternateColorCodes('&', 
                config.getString("gui.title", "&5LuxeAdd - Custom Items"));

        // ตรวจสอบชื่อ GUI
        if (!event.getView().getTitle().equals(expectedTitle)) {
            return;
        }

        event.setCancelled(true); // ป้องกันการหยิบไอเทมออกจาก GUI

        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();
        int targetSlot = config.getInt("items.sword_3d.slot", 13);

        if (event.getSlot() == targetSlot) {
            ItemStack itemToGive = event.getCurrentItem().clone();
            player.getInventory().addItem(itemToGive);
            
            String itemName = itemToGive.hasItemMeta() && itemToGive.getItemMeta().hasDisplayName()
                    ? itemToGive.getItemMeta().getDisplayName()
                    : itemToGive.getType().name();
                    
            player.sendMessage(ChatColor.GREEN + "Received " + itemName);
            player.closeInventory();
        }
    }
}
