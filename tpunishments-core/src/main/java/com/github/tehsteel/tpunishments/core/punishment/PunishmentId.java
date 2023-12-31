package com.github.tehsteel.tpunishments.core.punishment;

import java.util.UUID;

public record PunishmentId(String id) {

	private PunishmentId(final Builder builder) {
		this(builder.id);
	}

	public static class Builder {

		private final PunishmentType punishmentType;
		private String id;


		public Builder(final PunishmentType punishmentType) {
			if (punishmentType == null) {
				throw new IllegalArgumentException("Punishment type can not be null");
			}

			this.punishmentType = punishmentType;
		}

		public Builder withId(final String id) {
			this.id = id;

			return this;
		}

		public Builder withAutoGeneratedId() {
			id = punishmentType.getShortName() + "-" + UUID.randomUUID().toString().substring(0, 8);

			return this;
		}

		public PunishmentId build() {
			return new PunishmentId(this);
		}

	}
}
