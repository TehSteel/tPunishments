package com.github.tehsteel.tpunishments.plugin.report;

import com.github.tehsteel.tpunishments.core.report.Report;
import com.github.tehsteel.tpunishments.core.report.ReportId;
import com.github.tehsteel.tpunishments.plugin.util.HttpUtil;
import com.google.common.annotations.Beta;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public final class ReportManager {

	private final Cache<ReportId, Report> reportCache = CacheBuilder.newBuilder()
			.initialCapacity(100)
			.maximumSize(1000)
			.concurrencyLevel(100)
			.expireAfterWrite(Duration.ofHours(3))
			.build();

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
	 * Retrieves a cached list of reports.
	 *
	 * @param uuid The reporter uuid.
	 * @return A list of report.
	 */
	@ApiStatus.Experimental
	@Beta
	public List<Report> getCachedReports(final UUID uuid) {
		return reportCache.asMap().values().stream().filter(report -> report.getReporterUuid() == uuid).toList();
	}


	/**
	 * Retrieves a list of reports from the remote server.
	 *
	 * @param uuid The reporter uuid.
	 * @return A list of report.
	 */
	@ApiStatus.Experimental
	@Beta
	public CompletableFuture<List<Report>> getReportsByReporter(final UUID uuid) throws Exception {
		final List<Report> reports = HttpUtil.getReportsByReporter(uuid).orElse(null);


		if (reports != null && !reports.isEmpty()) {
			reports.forEach(this::addReportToCahce);
		}

		return CompletableFuture.supplyAsync(() -> reports);
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
