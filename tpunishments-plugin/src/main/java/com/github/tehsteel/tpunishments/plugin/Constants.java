package com.github.tehsteel.tpunishments.plugin;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public final class Constants {
	public static final String PLUGIN_NAME = PunishmentPlugin.getInstance().getName();
	public static String WEBSITE = "http://localhost:8080";
	public static String API_KEY = "XXX";
	public static boolean PAPI_ENABLED = false;

	private Constants() {
	}

	public static void loadConstants() {
		final FileConfiguration settingsCon = PunishmentPlugin.getInstance().getSettingsConfig().getConfig();
		final FileConfiguration messagesCon = PunishmentPlugin.getInstance().getMessagesConfig().getConfig();

		// Setup main locale
		Arrays.asList(Constants.Messages.Main.class.getFields()).forEach(field -> {
			try {
				field.set(String.class, messagesCon.getString("MAIN." + field.getName()));
			} catch (final IllegalAccessException ignore) {
			}
		});

		// Setup mute locale
		Arrays.asList(Constants.Messages.Mute.class.getFields()).forEach(field -> {
			try {
				field.set(String.class, messagesCon.getString("MUTE." + field.getName()));
			} catch (final IllegalAccessException ignore) {
			}
		});

		// Setup ban locale
		Arrays.asList(Constants.Messages.Ban.class.getFields()).forEach(field -> {
			try {
				field.set(String.class, messagesCon.getString("BAN." + field.getName()));
			} catch (final IllegalAccessException ignore) {
			}
		});

		// Setup kick locale
		Arrays.asList(Constants.Messages.Kick.class.getFields()).forEach(field -> {
			try {
				field.set(String.class, messagesCon.getString("KICK." + field.getName()));
			} catch (final IllegalAccessException ignore) {
			}
		});

		// Setup expires locale
		Arrays.asList(Constants.Messages.Expires.class.getFields()).forEach(field -> {
			try {
				field.set(String.class, messagesCon.getString("EXPIRES." + field.getName()));
			} catch (final IllegalAccessException ignore) {
			}
		});

		WEBSITE = settingsCon.getString("WEBSITE");
		API_KEY = settingsCon.getString("API_KEY");
	}


	public static class Messages {
		public static class Main {
			public static String PREFIX = "<gray>[<aqua>tPunishments<gray>]";
			public static String NO_PERMISSION = "<red>You don't have permission to use this command.";
			public static String PLAYER_NOT_ONLINE = "<red>That player isn't online!";
			public static String HELP = "<aqua>This server is running tPunishments v%version%<newline>Developed via heart by TehSteel <red>♥";
			public static String RELOAD = "<green>Plugin has been successfully reloaded";
		}

		public static class Ban {
			public static String USAGE = "<red>Correct Usage: /ban <player> <time> <reason>";
			public static String ALREADY_BANNED = "<red>That player is already banned!";
			public static String NOT_BANNED = "<red>That player is not banned!";
			public static String BROADCAST = "<red>%player% has banned %target% for %reason%";
			public static String BAN_MESSAGE_PERM = "<red>You've been permanently from X<newline>You've been banned for %reason%<newline>Ban ID: %banid%";
			public static String BAN_MESSAGE_TEMP = "<red>You've been temporarily banned for %time% from X<newline>You've been banned for %reason%<newline>Ban ID: %banid%";
			public static String UNBAN_USAGE = "<red>Correct Usage: /unban <player> <time> <reason>";
			public static String UNBAN_BROADCAST = "<red>%player% has unbanned %target% for %reason%";
		}

		public static class Kick {
			public static String USAGE = "<red>Correct Usage: /kick <player> <time> <reason>";
			public static String BROADCAST = "<red>%player% has kicked %target% for %reason%";
			public static String MESSAGE = "<red>You've been kicked from X<newline>You've been kicked for %reason%<newline>Kick ID: %kickid%";
		}


		public static class Mute {
			public static String USAGE = "<red>Correct Usage: /mute <player> <time> <reason>";

			public static String ALREADY_MUTED = "<red>That player is already muted!";
			public static String NOT_MUTED = "<red>That player is not muted!";
			public static String BROADCAST = "<red>%player% has muted %target% for %reason%";
			public static String MUTED = "<red>You're muted for %reason% expries in %time%<newline>Mute Id: %muteid%";
			public static String UNMUTE_USAGE = "<red>Correct Usage: /mute <player> <time> <reason>";
			public static String UNMUTE_BROADCAST = "<red>%player% has unmuted %target% for %reason%";
		}

		public static class Report {
			public static String USAGE = "<red>Correct Usage: /report <player> <reason>";
			public static String MESSAGE = "Thanks for reporting and keeping the community safe! Report Id: %reportid%";
			public static String COOLDOWN = "Heya watch your reports.";
		}

		public static class Expires {
			public static String NEVER = "NEVER";
			public static String YEARS = "Years";
			public static String MONTHS = "Months";
			public static String WEEKS = "Weeks";
			public static String DAYS = "Days";
			public static String HOURS = "Hours";
			public static String MINUTES = "Minutes";
			public static String SECONDS = "Seconds";
		}
	}
}

