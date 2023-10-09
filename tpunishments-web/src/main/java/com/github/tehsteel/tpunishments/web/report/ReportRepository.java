package com.github.tehsteel.tpunishments.web.report;

import com.github.tehsteel.tpunishments.core.report.Report;
import com.github.tehsteel.tpunishments.core.report.ReportId;
import com.github.tehsteel.tpunishments.web.report.model.ReportEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReportRepository extends MongoRepository<ReportEntity, ReportId> {
	Report findByReportId(ReportId reportId);
}
