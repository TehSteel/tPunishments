package com.github.tehsteel.tpunishments.plugin.punishment;

import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentId;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.plugin.util.HttpUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
public final class PunishmentManager {

	private final ConcurrentMap<PunishmentType, Cache<UUID, Punishment>> cacheConcurrentMap = new ConcurrentHashMap<>();

	public PunishmentManager() {
		Arrays.stream(PunishmentType.values()).forEach(punishmentType -> cacheConcurrentMap.put(punishmentType, CacheBuilder.newBuilder().expireAfterWrite(Duration.ofHours(2)).build()));
	}

	/**
	 * creates a new punishment and returns a CompletableFuture of the created punishment.
	 *
	 * @param punishment The punishment object to be created.
	 * @return A CompletableFuture containing the created punishment or the original punishment if creation fails.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public CompletableFuture<Punishment> createPunishment(final Punishment punishment) throws URISyntaxException, IOException, InterruptedException {
		final Punishment newPunishment = HttpUtil.createPunishment(punishment);

		if (newPunishment == null) return CompletableFuture.supplyAsync(() -> punishment);

		addPunishmentToCache(newPunishment);
		return CompletableFuture.supplyAsync(() -> newPunishment);
	}

	/**
	 * Retrieves an active punishment for a specific UUID and punishment type.
	 * If the punishment is not found in the cache, it fetches it from the remote server.
	 *
	 * @param uuid           The UUID of the player.
	 * @param punishmentType The type of punishment to retrieve.
	 * @return An active punishment object, or null if none is found.
	 * @throws URISyntaxException   If the URI syntax is incorrect.
	 * @throws IOException          If an IO error occurs.
	 * @throws InterruptedException If the operation is interrupted.
	 */
	public Optional<Punishment> getActivePunishment(final UUID uuid, final PunishmentType punishmentType) throws URISyntaxException, IOException, InterruptedException {
		Punishment punishment = cacheConcurrentMap.get(punishmentType).getIfPresent(uuid);

		if (punishment != null) {
			return Optional.empty();
		}

		punishment = HttpUtil.getActivePunishment(uuid, punishmentType);

		if (punishment != null) {
			addPunishmentToCache(punishment);
			return Optional.of(punishment);
		}

		return Optional.empty();
	}

	public Optional<Punishment> getPunishmentById(final PunishmentId punishmentId) throws URISyntaxException, IOException, InterruptedException {
		final Punishment punishment = HttpUtil.getPunishmentById(punishmentId);

		if (punishment != null) {
			addPunishmentToCache(punishment);
			return Optional.of(punishment);
		} else {
			return Optional.empty();
		}
	}

	private void addPunishmentToCache(@NonNull final Punishment punishment) {
		cacheConcurrentMap.get(punishment.getPunishmentType()).put(punishment.getUuid(), punishment);
	}
}
