package com.github.tehsteel.tpunishments.core.report;


import java.util.UUID;

public record Report(ReportId reportId,
					 UUID reporterUuid, UUID reportedUuid,
					 String reason, boolean resolved,
					 UUID resolver, String resolveReason) {

	private Report(final Builder builder) {
		this(builder.reportId,
				builder.reporterUuid, builder.reportedUuid,
				builder.reason, builder.resolved,
				builder.resolver, builder.resolveReason);
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

		public Builder withReporterUuid(final UUID reporterUuid) {
			this.reporterUuid = reporterUuid;

			return this;
		}

		public Builder withReportedUuid(final UUID reportedUuid) {
			this.reportedUuid = reportedUuid;

			return this;
		}

		public Builder withReason(final String reason) {
			this.reason = reason;

			return this;
		}

		public Builder withResolved(final boolean resolved) {
			this.resolved = resolved;

			return this;
		}

		public Builder withResolver(final UUID resolver) {
			this.resolver = resolver;

			return this;
		}

		public Builder withResolveReason(final String resolveReason) {
			this.resolveReason = resolveReason;

			return this;
		}

		public Report build() {
			return new Report(this);
		}
	}
}
