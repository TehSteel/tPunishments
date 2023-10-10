package com.github.tehsteel.tpunishments.plugin.listener;


import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.plugin.Constants;
import com.github.tehsteel.tpunishments.plugin.PunishmentPlugin;
import com.github.tehsteel.tpunishments.plugin.util.PlayerUtil;
import com.github.tehsteel.tpunishments.plugin.util.TimeUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public final class PlayerListener implements Listener {

	private final PunishmentPlugin plugin = PunishmentPlugin.getInstance();

	@EventHandler
	public void onAsyncPlayerPreLoginEvent(final AsyncPlayerPreLoginEvent event) {
		final UUID uuid = event.getUniqueId();
		final Punishment punishment;

		try {
			punishment = plugin.getPunishmentManager().getActivePunishment(uuid, PunishmentType.BAN).get();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		if (punishment == null) return;
		if (punishment.isExpired()) return;

		final String banMessage = punishment.isPermanent() ? Constants.Messages.Ban.BAN_MESSAGE_PERM : Constants.Messages.Ban.BAN_MESSAGE_TEMP;


		event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, MiniMessage.miniMessage().deserialize(
				banMessage
						.replace("%banid%", punishment.getPunishmentId().id())
						.replace("%reason%", punishment.getReason())
						.replace("%time%", TimeUtil.fromMillisToString((punishment.getEndTime() - System.currentTimeMillis())))
		));
	}

	@EventHandler
	public void onAsyncChatEvent(final AsyncChatEvent event) {
		final Player player = event.getPlayer();
		final Punishment punishment;

		try {
			punishment = plugin.getPunishmentManager().getActivePunishment(player.getUniqueId(), PunishmentType.MUTE).get();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		if (punishment == null) return;
		if (punishment.isExpired()) return;

		event.setCancelled(true);
		PlayerUtil.message(player, Constants.Messages.Mute.MUTED
				.replace("%reason%", punishment.getReason())
				.replace("%time%", TimeUtil.fromMillisToString(punishment.getEndTime() - System.currentTimeMillis()))
				.replace("%muteid%", punishment.getPunishmentId().id())
		);
	}
}
