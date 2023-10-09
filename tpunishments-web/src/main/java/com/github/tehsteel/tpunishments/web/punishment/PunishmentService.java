package com.github.tehsteel.tpunishments.web.punishment;

import com.github.tehsteel.tpunishments.core.punishment.PunishmentType;
import com.github.tehsteel.tpunishments.web.punishment.model.PunishmentEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
public final class PunishmentService {

	@Autowired
	private final PunishmentRepository punishmentRepository;

	@Autowired
	private final MongoTemplate mongoTemplate;

	public void insertPunishment(final PunishmentEntity punishment) {
		if (punishmentRepository.findById(punishment.getPunishmentId()).orElse(null) != null) {
			punishmentRepository.save(punishment);
			return;
		}

		punishmentRepository.insert(punishment);
	}

	public PunishmentEntity getActivePunishmentByType(final UUID uuid, final PunishmentType punishmentType) {
		final Query query = new Query(Criteria.where("punishment.uuid").is(uuid));

		query.addCriteria(new Criteria().orOperator(
				Criteria.where("punishment.endTime").gt(System.currentTimeMillis()),
				Criteria.where("punishment.endTime").is(-1L)
		));
		query.addCriteria(Criteria.where("punishment.pardoned").is(false));
		query.addCriteria(Criteria.where("punishment.punishmentType").is(punishmentType));

		return mongoTemplate.findOne(query, PunishmentEntity.class);
	}

	public List<PunishmentEntity> getAllPunishments(final UUID uuid) {
		final Query query = new Query(Criteria.where("punishment.uuid").is(uuid));

		return mongoTemplate.find(query, PunishmentEntity.class);
	}


	public List<PunishmentEntity> getAllPunishments() {
		return punishmentRepository.findAll();
	}
}
