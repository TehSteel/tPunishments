package com.github.tehsteel.tpunishments.plugin.command.punishment;

import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.core.util.TimeUtil;
import com.github.tehsteel.tpunishments.plugin.Constants;
import com.github.tehsteel.tpunishments.plugin.PunishmentPlugin;
import com.github.tehsteel.tpunishments.plugin.command.model.BaseCommand;
import com.github.tehsteel.tpunishments.plugin.punishment.PunishmentManager;
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

public final class MuteCommand extends BaseCommand {
	public MuteCommand() {
		super("mute");

		setAliases(List.of("tempmute"));
		setDescription("Mute a player");
		setPermission("tpunishments.command.mute");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player) ? (Player) sender : null;

		if (args.length < 2) {
			message(Constants.Messages.Mute.USAGE);
			return;
		}

		final OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

		try {
			final PunishmentManager punishmentManager = PunishmentPlugin.getInstance().getPunishmentManager();
			final Punishment punishment = punishmentManager.getActivePunishment(target.getUniqueId(), PunishmentType.MUTE).orElse(null);

			if (punishment != null && !punishment.isExpired()) {
				message(Constants.Messages.Mute.ALREADY_MUTED);
				return;
			}

			final long timeToAdd = TimeUtil.toMillisFromString(args[1]);
			final boolean isPermanent = timeToAdd == -1;
			final String reason = StringUtils.join(Arrays.copyOfRange(args, isPermanent ? 1 : 2, args.length), " ");

			final Punishment newPunishment = new Punishment.Builder(null, PunishmentType.MUTE, target.getUniqueId())
					.withPunisher(player == null ? null : player.getUniqueId())
					.withStartTime(System.currentTimeMillis())
					.withEndTime(isPermanent ? timeToAdd : (System.currentTimeMillis() + timeToAdd))
					.withReason(reason).build();

			final String playerName = (player == null) ? "CONSOLE" : player.getName();
			final String targetName = Objects.requireNonNull(target.getName());

			Bukkit.broadcast(MiniMessage.miniMessage().deserialize(Constants.Messages.Mute.BROADCAST
					.replace("%player%", playerName)
					.replace("%target%", targetName)
					.replace("%reason%", reason))
			);

			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				try {
					punishmentManager.createPunishment(newPunishment);
				} catch (final URISyntaxException | IOException | InterruptedException e) {
					throw new RuntimeException(e);
				}
			});

		} catch (URISyntaxException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
