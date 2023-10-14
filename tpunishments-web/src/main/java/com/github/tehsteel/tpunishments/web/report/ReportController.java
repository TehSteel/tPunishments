package com.github.tehsteel.tpunishments.web.report;


import com.github.tehsteel.tpunishments.core.report.Report;
import com.github.tehsteel.tpunishments.core.report.ReportId;
import com.github.tehsteel.tpunishments.web.report.model.ReportEntity;
import com.github.tehsteel.tpunishments.web.report.model.ReportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

	@Autowired
	private final ReportService reportService;


	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Report> create(@RequestBody final ReportRequest reportRequest) {

		final ReportId reportId = ReportId.Builder.createNewReportId();

		if (reportService.getReportRepository().findById(reportId).isPresent())
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);

		final Report report = new Report.Builder(reportId)
				.withReporterUuid(reportRequest.getReporterUuid())
				.withReportedUuid(reportRequest.getReportedUuid())
				.withReason(reportRequest.getReason())
				.withResolved(reportRequest.isResolved())
				.withResolver(reportRequest.getResolver())
				.withResolveReason(reportRequest.getResolveReason())
				.build();

		final ReportEntity reportEntity = new ReportEntity();

		reportEntity.setReportId(reportId);
		reportEntity.setReport(report);

		reportService.insertReport(reportEntity);

		return new ResponseEntity<>(report, HttpStatus.OK);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Report> update(@RequestBody final ReportRequest reportRequest) {
		final Report report = new Report.Builder(reportRequest.getReportId())
				.withReporterUuid(reportRequest.getReporterUuid())
				.withReportedUuid(reportRequest.getReportedUuid())
				.withReason(reportRequest.getReason())
				.withResolved(reportRequest.isResolved())
				.withResolver(reportRequest.getResolver())
				.withResolveReason(reportRequest.getResolveReason())
				.build();

		final ReportEntity reportEntity = new ReportEntity();
		reportEntity.setReportId(reportRequest.getReportId());
		reportEntity.setReport(report);
		reportService.insertReport(reportEntity);

		return new ResponseEntity<>(report, HttpStatus.OK);
	}

	@GetMapping("/get")
	public ResponseEntity<Report> getReportById(@RequestParam final ReportId reportId) {
		return new ResponseEntity<>(reportService.getReportRepository().findByReportId(reportId), HttpStatus.OK);
	}

	@GetMapping("/getReportsByReporter")
	public ResponseEntity<List<Report>> getReportsByReporter(@RequestParam final UUID uuid) {
		return new ResponseEntity<>(reportService.getReportsByReporter(uuid), HttpStatus.OK);
	}
}
