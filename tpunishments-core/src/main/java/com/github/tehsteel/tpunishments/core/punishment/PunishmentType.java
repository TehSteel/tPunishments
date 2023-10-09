package com.github.tehsteel.tpunishments.core.punishment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PunishmentType {
	WARN("warn", "wn"),
	MUTE("mute", "m"),
	KICK("kick", "k"),
	BAN("ban", "b");


	private final String
			name,
			shortName;


	public static PunishmentType valueOfString(final String type) {
		final PunishmentType[] types = PunishmentType.values();

		for (final PunishmentType punishmentType : types)
			if (punishmentType.name().equalsIgnoreCase(type))
				return punishmentType;


		return WARN;
	}
}
