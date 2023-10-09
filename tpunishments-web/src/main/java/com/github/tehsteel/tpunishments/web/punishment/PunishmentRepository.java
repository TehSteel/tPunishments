package com.github.tehsteel.tpunishments.web.punishment;

import com.github.tehsteel.tpunishments.core.punishment.PunishmentId;
import com.github.tehsteel.tpunishments.web.punishment.model.PunishmentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PunishmentRepository extends MongoRepository<PunishmentEntity, PunishmentId> {
	
}
