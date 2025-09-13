package dev.twice.astrostack;

import dev.twice.astrostack.config.ConfigManager;
import dev.twice.astrostack.listener.InventoryListener;
import dev.twice.astrostack.service.StackService;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class StackPlugin extends JavaPlugin {

    @Getter
    private ConfigManager configManager;

    @Getter
    private StackService stackService;

    private InventoryListener inventoryListener;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.stackService = new StackService(configManager);
        this.inventoryListener = new InventoryListener(stackService);

        getServer().getPluginManager().registerEvents(inventoryListener, this);
        getServer().getScheduler().runTask(this, stackService::processAllOnlinePlayersInventories);
    }

    public void reload() {
        configManager.reload();
        stackService.updateConfiguration();
    }
}