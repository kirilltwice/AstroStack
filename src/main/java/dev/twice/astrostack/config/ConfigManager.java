package dev.twice.astrostack.config;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ConfigManager {

    private final Plugin plugin;

    @Getter
    private int maxStackSize;

    @Getter
    private Set<Material> enabledMaterials;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        this.enabledMaterials = EnumSet.noneOf(Material.class);
        loadConfiguration();
    }

    public void reload() {
        plugin.reloadConfig();
        loadConfiguration();
    }

    private void loadConfiguration() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        int configuredSize = config.getInt("max-stack-size", 64);
        this.maxStackSize = Math.max(1, Math.min(configuredSize, 64));

        loadEnabledMaterials(config);
    }

    private void loadEnabledMaterials(FileConfiguration config) {
        enabledMaterials.clear();

        List<String> materialNames = config.getStringList("enabled-materials");

        if (materialNames.isEmpty()) {
            setDefaultMaterials();
        } else {
            for (String materialName : materialNames) {
                parseMaterial(materialName.toUpperCase().trim());
            }
        }
    }

    private void parseMaterial(String materialName) {
        try {
            Material material = Material.valueOf(materialName);
            enabledMaterials.add(material);
        } catch (IllegalArgumentException ignored) {}
    }

    private void setDefaultMaterials() {
        enabledMaterials.add(Material.POTION);
        enabledMaterials.add(Material.SPLASH_POTION);
        enabledMaterials.add(Material.LINGERING_POTION);
    }
}