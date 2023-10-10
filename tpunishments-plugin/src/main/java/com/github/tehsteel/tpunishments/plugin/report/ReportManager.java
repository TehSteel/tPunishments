package com.github.tehsteel.tpunishments.plugin.report;

import com.github.tehsteel.tpunishments.core.report.Report;
import com.github.tehsteel.tpunishments.core.report.ReportId;
import com.github.tehsteel.tpunishments.plugin.util.HttpUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.NonNull;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Getter
public final class ReportManager {

	private final Cache<ReportId, Report> reportCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofHours(1)).build();


	/**
	 * creates a new report and returns a CompletableFuture of the created report.
	 *
	 * @param report The report object to be created.
	 * @return A CompletableFuture containing the created report or the original report if creation fails.
	 * @throws Exception If any exception happens.
	 */
	public CompletableFuture<Report> createReport(final Report report) throws Exception {
		final Report newReport = HttpUtil.createReport(report).orElse(null);

		if (newReport == null) return CompletableFuture.supplyAsync(() -> report);

		addReportToCahce(newReport);
		return CompletableFuture.supplyAsync(() -> newReport);
	}

	/**
	 * Retrieves a report by id.
	 * If the report is not found in the cache, it fetches it from the remote server.
	 *
	 * @param reportId The id of the punishment.
	 * @return A report object, or null if none is found.
	 * @throws Exception If any exception happens.
	 */
	public CompletableFuture<Report> getReportById(final ReportId reportId) throws Exception {
		Report report = reportCache.getIfPresent(reportId);

		if (report == null) {
			report = HttpUtil.getReportById(reportId).orElse(null);
		}

		if (report != null) {
			addReportToCahce(report);
		}

		return CompletableFuture.supplyAsync(() -> reportCache.getIfPresent(reportId));
	}

	/**
	 * Adds the specified report to the cached data.
	 *
	 * @param report The report to be added. Must not be null.
	 */
	private void addReportToCahce(@NonNull final Report report) {
		reportCache.put(report.getReportId(), report);
	}
}
