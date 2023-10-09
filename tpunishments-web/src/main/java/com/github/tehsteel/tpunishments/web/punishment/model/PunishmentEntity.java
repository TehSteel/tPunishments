package com.github.tehsteel.tpunishments.web.punishment.model;


import com.github.tehsteel.tpunishments.core.punishment.Punishment;
import com.github.tehsteel.tpunishments.core.punishment.PunishmentId;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "punishments")
@Data
public final class PunishmentEntity {

	@Id @Indexed(unique = true) private PunishmentId punishmentId;
	private Punishment punishment;

}