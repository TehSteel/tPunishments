package com.github.tehsteel.tpunishments.core.report;

import lombok.Data;

import java.util.UUID;

@Data
public final class Report {

	private ReportId reportId;
	private UUID reporterUuid;
	private UUID reportedUuid;
	private String reason;
	private boolean resolved;
	private UUID resolver;
	private String resolveReason;

	private Report() {
	}

	private Report(final Builder builder) {
		this.reportId = builder.reportId;
		this.reporterUuid = builder.reporterUuid;
		this.reportedUuid = builder.reportedUuid;
		this.reason = builder.reason;
		this.resolved = builder.resolved;
		this.resolver = builder.resolver;
		this.resolveReason = builder.resolveReason;
	}


	public static class Builder {
		private final ReportId reportId;
		private UUID reporterUuid;
		private UUID reportedUuid;
		private String reason;
		private boolean resolved;
		private UUID resolver;
		private String resolveReason;

		public Builder(final ReportId reportId) {
			this.reportId = reportId;
		}

		public Report.Builder withReporterUuid(final UUID reporterUuid) {
			this.reporterUuid = reporterUuid;

			return this;
		}

		public Report.Builder withReportedUuid(final UUID reportedUuid) {
			this.reportedUuid = reportedUuid;

			return this;
		}

		public Report.Builder withReason(final String reason) {
			this.reason = reason;

			return this;
		}

		public Report.Builder withResolved(final boolean resolved) {
			this.resolved = resolved;

			return this;
		}

		public Report.Builder withResolver(final UUID resolver) {
			this.resolver = resolver;

			return this;
		}

		public Report.Builder withResolveReason(final String resolveReason) {
			this.resolveReason = resolveReason;

			return this;
		}

		public Report build() {
			return new Report(this);
		}
	}
}
