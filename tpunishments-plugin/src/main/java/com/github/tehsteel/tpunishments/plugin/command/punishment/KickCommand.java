package com.github.tehsteel.tpunishments.plugin.command.punishment;

import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.plugin.Constants;
import com.github.tehsteel.tpunishments.plugin.command.model.BaseCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

public final class KickCommand extends BaseCommand {
	public KickCommand() {
		super("kick");

		setDescription("Kick a player");
		setPermission("tpunishments.command.kick");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		final Player player = (sender instanceof Player) ? (Player) sender : null;

		if (args.length < 2) {
			message(Constants.Messages.Kick.USAGE);
			return;
		}

		final Player target = Bukkit.getPlayer(args[0]);

		if (target == null) {
			message(Constants.Messages.Main.PLAYER_NOT_ONLINE);
			return;
		}

		final String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");

		final Punishment punishment = new Punishment.Builder(null, PunishmentType.KICK, target.getUniqueId())
				.withPunisher(player == null ? null : player.getUniqueId())
				.withStartTime(System.currentTimeMillis())
				.withReason(reason).build();


		final String playerName = (player == null) ? "CONSOLE" : player.getName();
		final String targetName = Objects.requireNonNull(target.getName());

		Bukkit.broadcast(MiniMessage.miniMessage().deserialize(Constants.Messages.Kick.BROADCAST
				.replace("%player%", playerName)
				.replace("%target%", targetName)
				.replace("%reason%", reason))
		);

		try {
			plugin.getPunishmentManager().createPunishment(punishment).thenAccept((punishment1) -> {
				target.kick(MiniMessage.miniMessage().deserialize(
						Constants.Messages.Kick.MESSAGE
								.replace("%reason%", punishment.getReason())
								.replace("%kickid%", punishment1.getPunishmentId().id())
				));
			});
		} catch (URISyntaxException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
