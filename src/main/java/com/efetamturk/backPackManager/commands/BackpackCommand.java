package com.efetamturk.backPackManager.commands;

import com.efetamturk.backPackManager.BackPackManager;
import com.efetamturk.backPackManager.data.BackpackManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BackpackCommand implements CommandExecutor {
    private final BackPackManager plugin;

    public BackpackCommand(BackPackManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        player.openInventory(plugin.getBackpackManager().getBackpack(player.getUniqueId()));
        return true;
    }
}
