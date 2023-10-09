package com.github.tehsteel.tpunishments.web.report.model;

import com.github.tehsteel.tpunishments.core.report.Report;
import com.github.tehsteel.tpunishments.core.report.ReportId;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reports")
@Data
public final class ReportEntity {

	@Id @Indexed(unique = true) private ReportId reportId;
	private Report report;

}