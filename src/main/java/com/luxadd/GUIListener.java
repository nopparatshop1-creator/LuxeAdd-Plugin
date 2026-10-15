package com.luxadd;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(LuxeCommand.GUI_TITLE)) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == 13) {
            ItemStack itemToGive = event.getCurrentItem().clone();
            player.getInventory().addItem(itemToGive);
            player.sendMessage(ChatColor.GREEN + "Received " + itemToGive.getItemMeta().getDisplayName());
            player.closeInventory();
        }
    }
}
