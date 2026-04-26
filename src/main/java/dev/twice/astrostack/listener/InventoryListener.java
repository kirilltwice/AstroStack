package dev.twice.astrostack.listener;

import dev.twice.astrostack.service.StackService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryListener implements Listener {

    @NonNull @NotNull Plugin plugin;
    @NonNull @NotNull StackService stackService;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(@NonNull @NotNull final PlayerJoinEvent event) {
        stackService.processInventory(event.getPlayer().getInventory());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(@NonNull @NotNull final InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            scheduleInventoryProcessing(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryDrag(@NonNull @NotNull final InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            scheduleInventoryProcessing(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityPickupItem(@NonNull @NotNull final EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            scheduleInventoryProcessing(player);
        }
    }

    private void scheduleInventoryProcessing(@NonNull @NotNull final Player player) {
        plugin.getServer().getScheduler().runTask(plugin,
                () -> processPlayerInventory(player));
    }

    private void processPlayerInventory(@NonNull @NotNull final Player player) {
        if (!player.isOnline()) {
            return;
        }

        stackService.processInventory(player.getInventory());
    }
}
