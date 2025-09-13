package dev.twice.astrostack.service;

import dev.twice.astrostack.config.ConfigManager;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@RequiredArgsConstructor
public class StackService {

    private final ConfigManager configManager;

    public void processAllOnlinePlayersInventories() {
        Bukkit.getOnlinePlayers().forEach(this::processPlayerInventory);
    }

    public void processPlayerInventory(Player player) {
        processInventory(player.getInventory());
    }

    public void processInventory(Inventory inventory) {
        ItemStack[] contents = inventory.getContents();

        Arrays.stream(contents)
                .filter(this::shouldProcessItem)
                .forEach(this::updateItemStackSize);
    }

    private boolean shouldProcessItem(ItemStack item) {
        return item != null &&
                !item.getType().isAir() &&
                configManager.getEnabledMaterials().contains(item.getType());
    }

    private void updateItemStackSize(ItemStack item) {
        item.setData(DataComponentTypes.MAX_STACK_SIZE, configManager.getMaxStackSize());
    }

    public void updateConfiguration() {
        processAllOnlinePlayersInventories();
    }
}