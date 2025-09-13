package dev.twice.astrostack;

import dev.twice.astrostack.config.ConfigManager;
import dev.twice.astrostack.listener.InventoryListener;
import dev.twice.astrostack.service.StackService;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class StackPlugin extends JavaPlugin {

    private ConfigManager configManager;

    private StackService stackService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.configManager = new ConfigManager(this);
        this.stackService = new StackService(configManager);
        InventoryListener inventoryListener = new InventoryListener(stackService);

        getServer().getPluginManager().registerEvents(inventoryListener, this);
        getServer().getScheduler().runTaskLater(this,
                stackService::processAllOnlinePlayersInventories, 20L);
    }
}