package com.github.tehsteel.tpunishments.plugin.util;

import com.github.tehsteel.tpunishments.plugin.PunishmentPlugin;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public final class CustomConfig {

	private final File configFile;
	private FileConfiguration config;

	/**
	 * @param name The name of the configuration file (excluding file extension).
	 * @throws IOException                   If an IO error occurs.
	 * @throws InvalidConfigurationException If the configuration file is invalid.
	 */
	public CustomConfig(final String name) throws IOException, InvalidConfigurationException {
		configFile = new File(PunishmentPlugin.getInstance().getDataFolder(), name + ".yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			PunishmentPlugin.getInstance().saveResource(name + ".yml", false);
		}

		config = new YamlConfiguration();
		config.load(configFile);
	}

	/**
	 * Reloads the configuration file.
	 */
	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	/**
	 * Saves current configuration file
	 *
	 * @throws IOException If an IO error occurs.
	 */
	public void saveConfig() throws IOException {
		config.save(configFile);
		reloadConfig();
	}
}
