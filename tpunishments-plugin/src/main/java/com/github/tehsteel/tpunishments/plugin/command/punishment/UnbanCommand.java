package com.github.tehsteel.tpunishments.plugin.command.punishment;

import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.plugin.Constants;
import com.github.tehsteel.tpunishments.plugin.PunishmentPlugin;
import com.github.tehsteel.tpunishments.plugin.command.model.BaseCommand;
import com.github.tehsteel.tpunishments.plugin.util.HttpUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class UnbanCommand extends BaseCommand {
	public UnbanCommand() {
		super("unban");

		setAliases(List.of("pardon"));
		setDescription("Pardon a player");
		setPermission("tpunishments.command.unban");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player) ? (Player) sender : null;

		if (args.length < 2) {
			message(Constants.Messages.Ban.UNBAN_USAGE);
			return;
		}

		final OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		final String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");

		Bukkit.getScheduler().runTaskAsynchronously(PunishmentPlugin.getInstance(), () -> {
			try {
				final Punishment punishment = plugin.getPunishmentManager().getActivePunishment(target.getUniqueId(), PunishmentType.BAN).orElse(null);

				if (punishment == null) {
					message(Constants.Messages.Ban.NOT_BANNED);
					return;
				}

				punishment.setPardoned(true);
				punishment.setPardonInitiatorUuid(consoleCheck() ? null : Objects.requireNonNull(player).getUniqueId());
				punishment.setPardonReason(reason);
				punishment.setEndTime(System.currentTimeMillis());

				HttpUtil.updatePunishment(punishment);

			} catch (URISyntaxException | IOException | InterruptedException e) {
				throw new RuntimeException(e);
			}
		});

		final String playerName = (player == null) ? "CONSOLE" : player.getName();
		final String targetName = Objects.requireNonNull(target.getName());

		Bukkit.broadcast(MiniMessage.miniMessage().deserialize(Constants.Messages.Ban.UNBAN_BROADCAST
				.replace("%player%", playerName)
				.replace("%target%", targetName)
				.replace("%reason%", reason))
		);
	}

}
