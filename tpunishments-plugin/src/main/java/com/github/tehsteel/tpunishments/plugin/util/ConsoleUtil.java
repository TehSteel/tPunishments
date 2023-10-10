package com.github.tehsteel.tpunishments.plugin.util;

import com.github.tehsteel.tpunishments.plugin.Constants;
import lombok.NonNull;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

public final class ConsoleUtil {
	private ConsoleUtil() {
	}

	/**
	 * Logs a formatted message to the server console, with optional replacements.
	 *
	 * @param message The format string for the message.
	 * @param replace The objects to be used as replacements in the message format.
	 */
	public static void log(final String message, final Object... replace) {
		log(String.format(message, replace));
	}

	/**
	 * Logs a message to the server console.
	 *
	 * @param message The message to be logged. Non-null.
	 */
	public static void log(@NonNull final String message) {
		Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("[" + Constants.PLUGIN_NAME + "] " + message));
	}
}
