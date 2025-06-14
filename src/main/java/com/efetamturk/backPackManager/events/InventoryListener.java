package com.efetamturk.backPackManager.events;

import com.efetamturk.backPackManager.BackPackManager;
import com.efetamturk.backPackManager.data.BackpackManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (event.getView().getTitle().equals("Your Backpack")) {
            Player player = (Player) event.getPlayer();
            BackPackManager.getInstance().getBackpackManager().saveBackpack(player);
        }
    }
}
