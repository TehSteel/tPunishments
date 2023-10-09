package com.github.tehsteel.tpunishments.plugin.command.punishment;

import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.core.util.TimeUtil;
import com.github.tehsteel.tpunishments.plugin.Constants;
import com.github.tehsteel.tpunishments.plugin.PunishmentPlugin;
import com.github.tehsteel.tpunishments.plugin.command.model.BaseCommand;
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

public final class BanCommand extends BaseCommand {
	public BanCommand() {
		super("ban");


		setAliases(List.of("tempban"));
		setDescription("Ban a player");
		setPermission("tpunishments.command.ban");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player) ? (Player) sender : null;

		if (args.length < 2) {
			message(Constants.Messages.Ban.USAGE);
			return;
		}

		final OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

		try {
			final Punishment punishment = PunishmentPlugin.getInstance().getPunishmentManager().getActivePunishment(target.getUniqueId(), PunishmentType.BAN).orElse(null);

			if (punishment != null && !punishment.isExpired()) {
				message(Constants.Messages.Ban.ALREADY_BANNED);
				return;
			}

		} catch (URISyntaxException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}

		final long timeToAdd = TimeUtil.toMillisFromString(args[1]);
		final boolean isPermanent = timeToAdd == -1;
		final String reason = StringUtils.join(Arrays.copyOfRange(args, isPermanent ? 1 : 2, args.length), " ");

		final Punishment punishment = new Punishment.Builder(null, PunishmentType.BAN, target.getUniqueId())
				.withPunisher(player == null ? null : player.getUniqueId())
				.withStartTime(System.currentTimeMillis())
				.withEndTime(isPermanent ? timeToAdd : (System.currentTimeMillis() + timeToAdd))
				.withReason(reason)
				.build();

		final String playerName = (player == null) ? "CONSOLE" : player.getName();
		final String targetName = Objects.requireNonNull(target.getName());

		Bukkit.broadcast(MiniMessage.miniMessage().deserialize(Constants.Messages.Ban.BROADCAST
				.replace("%player%", playerName)
				.replace("%target%", targetName)
				.replace("%reason%", reason))
		);

		try {
			plugin.getPunishmentManager().createPunishment(punishment).thenAccept((newPunishment) -> {
				if (target.isOnline()) {
					final Player targetPlayer = target.getPlayer();
					if (targetPlayer != null) {
						if (newPunishment.isPermanent()) {
							targetPlayer.kick(MiniMessage.miniMessage().deserialize(
									Constants.Messages.Ban.BAN_MESSAGE_PERM
											.replace("%reason%", reason)
											.replace("%banid%", newPunishment.getPunishmentId().id())
							));
						} else {
							targetPlayer.kick(MiniMessage.miniMessage().deserialize(Constants.Messages.Ban.BAN_MESSAGE_TEMP
									.replace("%reason%", newPunishment.getReason())
									.replace("%time%", TimeUtil.fromMillisToString((newPunishment.getEndTime() - System.currentTimeMillis())))
									.replace("%banid%", newPunishment.getPunishmentId().id())
							));
						}
					}
				}
			});
		} catch (final URISyntaxException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
