package com.github.tehsteel.tpunishments.web.report.model;

import com.github.tehsteel.tpunishments.core.report.ReportId;
import lombok.Data;

import java.util.UUID;

@Data
public final class ReportRequest {
	private ReportId reportId;
	private UUID reporterUuid;
	private UUID reportedUuid;
	private String reason;
	private boolean resolved;
	private UUID resolver;
	private String resolveReason;
}
