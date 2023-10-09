package com.github.tehsteel.tpunishments.plugin.command.model;

import com.github.tehsteel.tpunishments.plugin.Constants;
import com.github.tehsteel.tpunishments.plugin.PunishmentPlugin;
import com.github.tehsteel.tpunishments.plugin.util.PlayerUtil;
import lombok.NonNull;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand extends Command {
	protected final PunishmentPlugin plugin = PunishmentPlugin.getInstance();

	private CommandSender sender;
	private Player player;

	public BaseCommand(final String name) {
		super(name);

		permissionMessage(MiniMessage.miniMessage().deserialize(Constants.Messages.Main.NO_PERMISSION));
	}

	public void permissionMessage(@NonNull final String permissionMessage) {
		super.permissionMessage(MiniMessage.miniMessage().deserialize(permissionMessage));
	}

	protected abstract void run(CommandSender sender, String[] args);

	@Override
	public boolean execute(@NonNull final CommandSender sender, @NonNull final String command, @NonNull final String[] args) {
		this.sender = sender;
		run(sender, args);
		return true;
	}


	/**
	 * Short function to the PlayerUtil class.
	 *
	 * @param text will be sent to the player.
	 */
	protected void message(final String text) {
		PlayerUtil.message(sender, text);
	}

	/**
	 * Checks if the executer is a console.
	 *
	 * @return true if console executed.
	 */
	protected boolean consoleCheck() {
		return !(sender instanceof Player);
	}
}
