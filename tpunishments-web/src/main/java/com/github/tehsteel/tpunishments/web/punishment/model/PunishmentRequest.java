package com.github.tehsteel.tpunishments.web.punishment.model;

import com.github.tehsteel.tpunishments.core.punishment.PunishmentId;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import lombok.Data;

import java.util.UUID;

@Data
public class PunishmentRequest {
	private PunishmentId punishmentId;
	private String punishmentType;
	private UUID uuid;
	private UUID punisher;
	private long startTime;
	private long endTime;
	private String reason;
	private boolean pardoned;
	private UUID pardonInitiatorUuid;
	private String pardonReason;


	public PunishmentType getPunishmentType() {
		return PunishmentType.valueOfString(punishmentType);
	}
}