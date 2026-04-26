package dev.twice.astrostack.service;

import dev.twice.astrostack.config.StackSettings;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StackService {

    @NonNull @NotNull StackSettings stackSettings;

    public void processAllOnlinePlayersInventories() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            processInventory(player.getInventory());
        }
    }

    public void processInventory(@NonNull @NotNull final Inventory inventory) {
        final ItemStack[] contents = inventory.getContents();

        normalizeStackSizes(contents);
        mergeSimilarStacks(contents);

        inventory.setContents(contents);
    }

    private void normalizeStackSizes(@NonNull @NotNull final ItemStack[] contents) {
        for (final ItemStack item : contents) {
            if (shouldProcessItem(item)) {
                updateItemStackSize(item);
            }
        }
    }

    private void mergeSimilarStacks(@NonNull @NotNull final ItemStack[] contents) {
        final int maxStackSize = stackSettings.getMaxStackSize();

        for (int sourceSlot = 0; sourceSlot < contents.length; sourceSlot++) {
            final ItemStack source = contents[sourceSlot];

            if (!shouldProcessItem(source) || source.getAmount() >= maxStackSize) {
                continue;
            }

            fillStack(contents, sourceSlot, source, maxStackSize);
        }
    }

    private void fillStack(
            @NonNull @NotNull final ItemStack[] contents,
            final int sourceSlot,
            @NonNull @NotNull final ItemStack source,
            final int maxStackSize
    ) {
        for (int targetSlot = sourceSlot + 1; targetSlot < contents.length; targetSlot++) {
            final ItemStack target = contents[targetSlot];

            if (!canMerge(source, target)) {
                continue;
            }

            final int freeSpace = maxStackSize - source.getAmount();
            final int amountToMove = Math.min(freeSpace, target.getAmount());

            source.setAmount(source.getAmount() + amountToMove);
            target.setAmount(target.getAmount() - amountToMove);

            if (target.getAmount() <= 0) {
                contents[targetSlot] = null;
            }

            if (source.getAmount() >= maxStackSize) {
                return;
            }
        }
    }

    private boolean canMerge(@NonNull @NotNull final ItemStack source, @Nullable final ItemStack target) {
        return shouldProcessItem(target) && source.isSimilar(target);
    }

    private boolean shouldProcessItem(@Nullable final ItemStack item) {
        return item != null
                && !item.getType().isAir()
                && stackSettings.getEnabledMaterials().contains(item.getType());
    }

    private void updateItemStackSize(@NonNull @NotNull final ItemStack item) {
        item.setData(DataComponentTypes.MAX_STACK_SIZE, stackSettings.getMaxStackSize());
    }
}
