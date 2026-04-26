package dev.twice.astrostack.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigManager implements StackSettings {

    static final String KEY_MAX_STACK_SIZE = "max-stack-size";
    static final String KEY_ENABLED_MATERIALS = "enabled-materials";

    static final int MIN_STACK_SIZE = 1;
    static final int MAX_STACK_SIZE = 64;

    @NonNull @NotNull Plugin plugin;

    @ToString.Include
    int maxStackSize;

    @NotNull Set<Material> enabledMaterials;

    public ConfigManager(@NonNull @NotNull final Plugin plugin) {
        this.plugin = plugin;

        final FileConfiguration config = plugin.getConfig();
        this.maxStackSize = readMaxStackSize(config);
        this.enabledMaterials = readEnabledMaterials(config);
    }

    @Override
    public @NotNull @Unmodifiable Set<Material> getEnabledMaterials() {
        return Collections.unmodifiableSet(enabledMaterials);
    }

    private int readMaxStackSize(@NonNull @NotNull final FileConfiguration config) {
        final int configuredSize = config.getInt(KEY_MAX_STACK_SIZE, MAX_STACK_SIZE);

        if (configuredSize < MIN_STACK_SIZE || configuredSize > MAX_STACK_SIZE) {
            plugin.getLogger().warning(
                    "Invalid " + KEY_MAX_STACK_SIZE + ": " + configuredSize
                            + ", clamping to [" + MIN_STACK_SIZE + ", " + MAX_STACK_SIZE + "]"
            );
        }

        return Math.max(MIN_STACK_SIZE, Math.min(configuredSize, MAX_STACK_SIZE));
    }

    private @NotNull Set<Material> readEnabledMaterials(@NonNull @NotNull final FileConfiguration config) {
        final EnumSet<Material> materials = EnumSet.noneOf(Material.class);
        final List<String> materialNames = config.getStringList(KEY_ENABLED_MATERIALS);

        if (materialNames.isEmpty()) {
            addDefaultMaterials(materials);
            return materials;
        }

        for (final String materialName : materialNames) {
            final Material material = parseMaterial(materialName);

            if (material != null) {
                materials.add(material);
            }
        }

        return materials;
    }

    private @Nullable Material parseMaterial(@NonNull @NotNull final String materialName) {
        final String normalizedName = materialName.trim().toUpperCase(Locale.ROOT);

        if (normalizedName.isEmpty()) {
            return null;
        }

        try {
            return Material.valueOf(normalizedName);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid material in config: " + normalizedName);
            return null;
        }
    }

    private void addDefaultMaterials(@NonNull @NotNull final Set<Material> materials) {
        materials.add(Material.POTION);
        materials.add(Material.SPLASH_POTION);
        materials.add(Material.LINGERING_POTION);
    }
}
