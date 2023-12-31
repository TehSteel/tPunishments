package com.github.tehsteel.tpunishments.plugin.command.report;

import com.github.tehsteel.tpunishments.core.report.Report;
import com.github.tehsteel.tpunishments.plugin.Constants;
import com.github.tehsteel.tpunishments.plugin.PunishmentPlugin;
import com.github.tehsteel.tpunishments.plugin.command.model.BaseCommand;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class ReportCommand extends BaseCommand {
	private final Cache<UUID, Boolean> cacheData = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();

	public ReportCommand() {
		super("report");

		setDescription("Create a new report");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		if (!(sender instanceof final Player player)) return;

		if (args.length < 2) {
			message(Constants.Messages.Report.USAGE);
			return;
		}

		if (isOnCooldown(player.getUniqueId())) {
			message(Constants.Messages.Report.COOLDOWN);
			return;
		}

		final Player target = Bukkit.getPlayer(args[0]);

		if (target == null) {
			message(Constants.Messages.Main.PLAYER_NOT_ONLINE);
			return;
		}

		final String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");

		final Report report = new Report.Builder(null)
				.withReporterUuid(player.getUniqueId())
				.withReportedUuid(target.getUniqueId())
				.withReason(reason)
				.build();


		try {
			PunishmentPlugin.getInstance().getReportManager().createReport(report).thenAccept((newReport) -> {
				message(Constants.Messages.Report.MESSAGE
						.replace("%reportid%", newReport.getReportId().id())
						.replace("%target%", target.getName())
						.replace("%reason%", reason)
				);
				cacheData.put(player.getUniqueId(), true);
			});
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isOnCooldown(final UUID uuid) {
		return cacheData.asMap().containsKey(uuid);
	}
}
