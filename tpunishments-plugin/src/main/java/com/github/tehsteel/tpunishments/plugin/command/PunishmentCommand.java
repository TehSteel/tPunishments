package com.github.tehsteel.tpunishments.plugin.command;

import com.github.tehsteel.tpunishments.plugin.Constants;
import com.github.tehsteel.tpunishments.plugin.command.model.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class PunishmentCommand extends BaseCommand {
	public PunishmentCommand() {
		super("tpunishments");


		setAliases(List.of("punishments", "punishment"));
		setDescription("General Command");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		if (args.length == 0) {
			message(Constants.Messages.Main.HELP.replace("%version%", plugin.getPluginMeta().getVersion()));
			return;
		}

		if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("tpunishments.command.reload")) {
				message(Constants.Messages.Main.NO_PERMISSION);
				return;
			}
			plugin.getSettingsConfig().reloadConfig();
			plugin.getMessagesConfig().reloadConfig();
			Constants.loadConstants();
			message(Constants.Messages.Main.RELOAD);
			return;
		}
	}
}
