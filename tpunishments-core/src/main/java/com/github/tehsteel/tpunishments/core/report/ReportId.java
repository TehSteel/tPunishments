package com.github.tehsteel.tpunishments.core.report;

import java.util.UUID;

public record ReportId(String id) {


	private ReportId(final Builder builder) {
		this(builder.id);
	}

	public static class Builder {
		private final String id;

		public Builder(final String id) {
			this.id = id;
		}

		public static ReportId createNewReportId() {
			return new ReportId("re-" + UUID.randomUUID().toString().substring(0, 8));
		}

		public ReportId build() {
			return new ReportId(this);
		}
	}
}
