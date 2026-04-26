package dev.twice.astrostack;

import dev.twice.astrostack.config.ConfigManager;
import dev.twice.astrostack.listener.InventoryListener;
import dev.twice.astrostack.service.StackService;
import org.bukkit.plugin.java.JavaPlugin;

public class StackPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        final ConfigManager configManager = new ConfigManager(this);
        final StackService stackService = new StackService(configManager);

        final InventoryListener inventoryListener = new InventoryListener(this, stackService);

        getServer().getPluginManager().registerEvents(inventoryListener, this);
        getServer().getScheduler().runTaskTimer(this,
                stackService::processAllOnlinePlayersInventories, 20L, 100L);

    }

    @Override
    public void onDisable() {
        getLogger().info("AstroStack disabled.");
    }
}
