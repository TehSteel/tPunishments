package com.github.tehsteel.tpunishments.plugin.command.report;

import com.github.tehsteel.tpunishments.plugin.command.model.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ReportsCommand extends BaseCommand {
	public ReportsCommand() {
		super("reports");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		if (!(sender instanceof final Player player)) return;
	}
}
