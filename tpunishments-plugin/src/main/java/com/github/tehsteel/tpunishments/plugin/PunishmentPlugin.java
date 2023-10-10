package com.github.tehsteel.tpunishments.plugin;

import com.github.tehsteel.tpunishments.plugin.command.PunishmentCommand;
import com.github.tehsteel.tpunishments.plugin.command.punishment.*;
import com.github.tehsteel.tpunishments.plugin.listener.PlayerListener;
import com.github.tehsteel.tpunishments.plugin.punishment.PunishmentManager;
import com.github.tehsteel.tpunishments.plugin.report.ReportManager;
import com.github.tehsteel.tpunishments.plugin.util.ConsoleUtil;
import com.github.tehsteel.tpunishments.plugin.util.CustomConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;


public final class PunishmentPlugin extends JavaPlugin {
	@Getter private static PunishmentPlugin instance;

	@Getter private PunishmentManager punishmentManager;
	@Getter private ReportManager reportManager;
	@Getter private CustomConfig settingsConfig, messagesConfig;

	@Override
	public void onEnable() {
		super.onEnable();

		instance = this;


		registerFiles();
		registerManagers();
		registerCommands();
		registerListeners();

		performTestRequest();
	}

	@Override
	public void onDisable() {
		super.onDisable();

		instance = null;
	}

	private void registerFiles() {
		ConsoleUtil.log("<gray>Registering files...<gray>");
		final long startTime = System.currentTimeMillis();
		try {
			settingsConfig = new CustomConfig("settings");
			messagesConfig = new CustomConfig("messages");
			Constants.loadConstants();
		} catch (IOException | InvalidConfigurationException e) {
			ConsoleUtil.log("<red>Failed to register files.</red>");
			throw new RuntimeException(e);
		}

		ConsoleUtil.log("<green>Registered files successfully in %s ms.</green>", (System.currentTimeMillis() - startTime));
	}


	private void registerCommands() {
		ConsoleUtil.log("Registering commands...");
		final long startTime = System.currentTimeMillis();


		Arrays.asList(
				new UnbanCommand(),
				new BanCommand(),
				new KickCommand(),
				new MuteCommand(),
				new UnmuteCommand(),
				new PunishmentCommand()
		).forEach(this::registerCommand);

		ConsoleUtil.log("<green>Registered commands successfully in %s ms.</green>", (System.currentTimeMillis() - startTime));
	}

	private void registerListeners() {
		ConsoleUtil.log("Registering events...");
		final long startTime = System.currentTimeMillis();
		List.of(
				new PlayerListener()
		).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

		ConsoleUtil.log("<green>Registered listeners successfully in %s ms.</green>", (System.currentTimeMillis() - startTime));
	}

	private void registerManagers() {
		punishmentManager = new PunishmentManager();
		reportManager = new ReportManager();

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			Constants.PAPI_ENABLED = true;
		}
	}

	/**
	 * Registers a command in the server's command map.
	 *
	 * @param command The command to be registered.
	 */
	private void registerCommand(final Command command) {
		try {
			final Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			final CommandMap commandMap = (CommandMap) commandMapField.get(getServer());
			commandMap.register(command.getLabel(), command);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void performTestRequest() {
		ConsoleUtil.log("Sending a test request...");

		try {

			final HttpClient client = HttpClient.newHttpClient();
			final HttpRequest request = HttpRequest.newBuilder()
					.uri(new URI(String.format("%s/test", Constants.WEBSITE)))
					.GET()
					.header("Content-Type", "application/json")
					.header("X-API-KEY", Constants.API_KEY)
					.timeout(Duration.ofSeconds(30))
					.build();
			final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				ConsoleUtil.log("<red>There was an error while sending the test request, Status Code: " + response.statusCode());
				Bukkit.getPluginManager().disablePlugin(this);
			} else {
				ConsoleUtil.log("<green>The test request was sent successfully and received a response. Status Code: " + response.statusCode());

			}
		} catch (IOException | InterruptedException | URISyntaxException e) {
			throw new RuntimeException(e);
		}


	}

}
