package com.github.tehsteel.tpunishments.core.punishment;

import lombok.Data;

import java.util.UUID;

@Data
public final class Punishment {
	private PunishmentId punishmentId;
	private PunishmentType punishmentType;
	private UUID uuid;
	private UUID punisher;
	private long startTime;
	private long endTime;
	private String reason;
	private boolean pardoned;
	private UUID pardonInitiatorUuid;
	private String pardonReason;


	private Punishment() {
	}

	private Punishment(final Builder builder) {
		this.punishmentId = builder.punishmentId;
		this.punishmentType = builder.punishmentType;
		this.uuid = builder.uuid;
		this.punisher = builder.punisher;
		this.startTime = builder.startTime;
		this.endTime = builder.endTime;
		this.reason = builder.reason;
		this.pardoned = builder.pardoned;
		this.pardonInitiatorUuid = builder.pardonInitiatorUuid;
		this.pardonReason = builder.pardonReason;
	}

	public boolean isExpired() {
		return !isPermanent() && System.currentTimeMillis() > endTime;
	}

	public boolean isPermanent() {
		return endTime == -1;
	}


	public static class Builder {
		private final PunishmentId punishmentId;
		private final PunishmentType punishmentType;
		private final UUID uuid;
		private UUID punisher;
		private long startTime;
		private long endTime;
		private String reason;
		private boolean pardoned;
		private UUID pardonInitiatorUuid;
		private String pardonReason;


		public Builder(final PunishmentId punishmentId, final PunishmentType punishmentType, final UUID uuid) {
			if (punishmentType == null || uuid == null)
				throw new IllegalArgumentException("Punishment type / uuid cannot be null");

			this.punishmentId = punishmentId;
			this.punishmentType = punishmentType;
			this.uuid = uuid;
		}


		public Punishment.Builder withPunisher(final UUID punisher) {
			this.punisher = punisher;

			return this;
		}


		public Punishment.Builder withStartTime(final long startTime) {
			this.startTime = startTime;

			return this;
		}

		public Punishment.Builder withEndTime(final long endTime) {
			this.endTime = endTime;

			return this;
		}

		public Punishment.Builder withReason(final String reason) {
			this.reason = reason;

			return this;
		}

		public Punishment.Builder withPardoned(final boolean pardoned) {
			this.pardoned = pardoned;

			return this;
		}

		public Punishment.Builder withPardonInitiatorUuid(final UUID pardonInitiatorUuid) {
			this.pardonInitiatorUuid = pardonInitiatorUuid;

			return this;
		}

		public Punishment.Builder withPardonReason(final String pardonReason) {
			this.pardonReason = pardonReason;

			return this;
		}

		public Punishment build() {
			return new Punishment(this);
		}
	}
}

