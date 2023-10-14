package com.github.tehsteel.tpunishments.web.report;

import com.github.tehsteel.tpunishments.core.report.Report;
import com.github.tehsteel.tpunishments.web.report.model.ReportEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Getter
@RequiredArgsConstructor
public final class ReportService {

	@Autowired
	private final ReportRepository reportRepository;

	@Autowired
	private final MongoTemplate mongoTemplate;

	public void insertReport(final ReportEntity report) {
		if (reportRepository.findById(report.getReportId()).orElse(null) != null) {
			reportRepository.save(report);
			return;
		}

		reportRepository.insert(report);
	}

	public List<Report> getReportsByReporter(final UUID uuid) {
		final Query query = new Query(Criteria.where("report.reporterUuid").is(uuid));

		final List<Report> reports = new ArrayList<>();

		mongoTemplate.find(query, ReportEntity.class).forEach(reportEntity -> reports.add(reportEntity.getReport()));

		return reports;
	}
}
