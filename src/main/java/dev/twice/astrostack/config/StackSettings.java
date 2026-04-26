package dev.twice.astrostack.config;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public interface StackSettings {

    int getMaxStackSize();

    @NotNull
    @Unmodifiable
    Set<Material> getEnabledMaterials();
}
