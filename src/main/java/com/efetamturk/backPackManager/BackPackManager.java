package com.efetamturk.backPackManager;

import com.efetamturk.backPackManager.commands.BackpackCommand;
import com.efetamturk.backPackManager.data.BackpackManager;
import com.efetamturk.backPackManager.events.InventoryListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BackPackManager extends JavaPlugin {

    private static BackPackManager instance;
    private BackpackManager backpackManager;

    @Override
    public void onEnable() {
        instance = this;
        backpackManager = new BackpackManager();
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getCommand("backpack").setExecutor(new BackpackCommand(this));
        getLogger().info("BackpackPlugin enabled.");
    }

    @Override
    public void onDisable() {

    }

    public BackpackManager getBackpackManager() {
        return backpackManager;
    }

    public static BackPackManager getInstance() {
        return instance;
    }
}
