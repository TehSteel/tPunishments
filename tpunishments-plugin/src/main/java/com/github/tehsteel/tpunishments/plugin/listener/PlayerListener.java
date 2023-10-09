package com.github.tehsteel.tpunishments.plugin.listener;


import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.core.util.TimeUtil;
import com.github.tehsteel.tpunishments.plugin.Constants;
import com.github.tehsteel.tpunishments.plugin.PunishmentPlugin;
import com.github.tehsteel.tpunishments.plugin.util.PlayerUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

public final class PlayerListener implements Listener {

	@EventHandler
	public void onAsyncPlayerPreLoginEvent(final AsyncPlayerPreLoginEvent event) {
		final UUID uuid = event.getUniqueId();
		final Punishment punishment;

		try {
			punishment = PunishmentPlugin.getInstance().getPunishmentManager().getActivePunishment(uuid, PunishmentType.BAN).orElse(null);
		} catch (URISyntaxException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}

		if (punishment == null) return;

		if (!punishment.isExpired()) {
			if (punishment.isPermanent()) {
				event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, MiniMessage.miniMessage().deserialize(
						Constants.Messages.Ban.BAN_MESSAGE_PERM
								.replace("%banid%", punishment.getPunishmentId().id())
								.replace("%reason%", punishment.getReason())
								.replace("%time%", TimeUtil.fromMillisToString((punishment.getEndTime() - System.currentTimeMillis())))
				));
			} else {
				event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, MiniMessage.miniMessage().deserialize(
						Constants.Messages.Ban.BAN_MESSAGE_TEMP
								.replace("%banid%", punishment.getPunishmentId().id())
								.replace("%reason%", punishment.getReason())
								.replace("%time%", TimeUtil.fromMillisToString((punishment.getEndTime() - System.currentTimeMillis())))
				));
			}
		}
	}

	@EventHandler
	public void onAsyncChatEvent(final AsyncChatEvent event) {
		final Player player = event.getPlayer();
		final Punishment punishment;

		try {
			punishment = PunishmentPlugin.getInstance().getPunishmentManager()
					.getActivePunishment(player.getUniqueId(), PunishmentType.MUTE).orElse(null);
		} catch (URISyntaxException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}

		if (punishment == null) return;

		if (!punishment.isExpired()) {
			event.setCancelled(true);
			PlayerUtil.message(player, Constants.Messages.Mute.MUTED
					.replace("%reason%", punishment.getReason())
					.replace("%time%", TimeUtil.fromMillisToString(punishment.getEndTime() - System.currentTimeMillis()))
					.replace("%muteid%", punishment.getPunishmentId().id())
			);
		}
	}
}
