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

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (shouldProcessItem(item)) {
                ItemStack updatedItem = updateItemStackSize(item);
                inventory.setItem(i, updatedItem);
            }
        }
    }

    public boolean shouldProcessItem(ItemStack item) {
        return item != null &&
                !item.getType().isAir() &&
                configManager.getEnabledMaterials().contains(item.getType());
    }

    public ItemStack updateItemStackSize(ItemStack item) {
        ItemStack newItem = item.clone();
        newItem.setData(DataComponentTypes.MAX_STACK_SIZE, configManager.getMaxStackSize());
        return newItem;
    }
}