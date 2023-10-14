package com.github.tehsteel.tpunishments.plugin.punishment;

import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentId;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.plugin.util.HttpUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.NonNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
public final class PunishmentManager {

	private final ConcurrentMap<PunishmentType, Cache<UUID, Punishment>> cacheConcurrentMap = new ConcurrentHashMap<>();

	public PunishmentManager() {
		Arrays.stream(PunishmentType.values()).forEach(punishmentType -> cacheConcurrentMap.put(punishmentType, CacheBuilder.newBuilder()
				.initialCapacity(100)
				.maximumSize(1000)
				.concurrencyLevel(100)
				.expireAfterWrite(Duration.ofHours(3))
				.build()));
	}

	/**
	 * creates a new punishment and returns a CompletableFuture of the created punishment.
	 *
	 * @param punishment The punishment object to be created.
	 * @return A CompletableFuture containing the created punishment or the original punishment if creation fails.
	 * @throws Exception If any exception happens.
	 */
	public CompletableFuture<Punishment> createPunishment(final Punishment punishment) throws Exception {
		final Punishment newPunishment = HttpUtil.createPunishment(punishment).orElse(null);

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
	 * @throws Exception If any exception happens.
	 */
	public CompletableFuture<Punishment> getActivePunishment(final UUID uuid, final PunishmentType punishmentType) throws Exception {
		Punishment punishment = cacheConcurrentMap.get(punishmentType).getIfPresent(uuid);

		if (punishment != null) {
			final Punishment finalPunishment = punishment;
			return CompletableFuture.supplyAsync(() -> finalPunishment);
		}

		punishment = HttpUtil.getActivePunishment(uuid, punishmentType).orElse(null);

		if (punishment != null) {
			final Punishment finalPunishment = punishment;
			addPunishmentToCache(finalPunishment);
			return CompletableFuture.supplyAsync(() -> finalPunishment);
		}

		return CompletableFuture.supplyAsync(() -> null);
	}

	/**
	 * Retrieves a punishment by id.
	 * It always fetches it from the remote server.
	 *
	 * @param punishmentId The id of the punishment.
	 * @return An active punishment, or null if none is found.
	 * @throws Exception If any exception happens.
	 */
	public CompletableFuture<Punishment> getPunishmentById(final PunishmentId punishmentId) throws Exception {
		final Punishment punishment = HttpUtil.getPunishmentById(punishmentId).orElse(null);

		if (punishment != null) {
			addPunishmentToCache(punishment);
		}

		return CompletableFuture.supplyAsync(() -> punishment);
	}

	/**
	 * Adds a punishment to the cache, Must be not null.
	 *
	 * @param punishment The punishment to add to the cache.
	 */
	private void addPunishmentToCache(@NonNull final Punishment punishment) {
		cacheConcurrentMap.get(punishment.getPunishmentType()).put(punishment.getUuid(), punishment);
	}
}
