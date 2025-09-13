package dev.twice.astrostack.service;

import dev.twice.astrostack.config.ConfigManager;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class StackService {

    private final ConfigManager configManager;

    public void processAllOnlinePlayersInventories() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            processInventory(player.getInventory());
        }
    }

    public void processInventory(Inventory inventory) {
        ItemStack[] contents = inventory.getContents();

        for (ItemStack item : contents) {
            if (shouldProcessItem(item)) {
                updateItemStackSize(item);
            }
        }
    }

    public boolean shouldProcessItem(ItemStack item) {
        return item != null &&
                !item.getType().isAir() &&
                configManager.getEnabledMaterials().contains(item.getType());
    }
    public void updateItemStackSize(ItemStack item) {
        item.setData(DataComponentTypes.MAX_STACK_SIZE, configManager.getMaxStackSize());
    }
}