package com.github.tehsteel.tpunishments.plugin.util;

import com.github.tehsteel.tpunishments.plugin.Constants;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class provides utility methods for players.
 */
public final class PlayerUtil {
	private PlayerUtil() {
	}

	/**
	 * Sends a message to a player, optionally processing PlaceholderAPI placeholders if enabled.
	 *
	 * @param player  The player to send the message to.
	 * @param message The message to send.
	 */
	public static void message(final Player player, String message) {
		if (Constants.PAPI_ENABLED) {
			message = PlaceholderAPI.setPlaceholders(player, message);
		}

		player.sendMessage(MiniMessage.miniMessage().deserialize(message));
	}

	/**
	 * Sends a message to a command sender with a predefined prefix.
	 *
	 * @param sender  The command sender to send the message to.
	 * @param message The message to send.
	 */
	public static void message(final CommandSender sender, final String message) {
		sender.sendMessage(MiniMessage.miniMessage().deserialize(Constants.Messages.Main.PREFIX + " " + message));
	}

}
