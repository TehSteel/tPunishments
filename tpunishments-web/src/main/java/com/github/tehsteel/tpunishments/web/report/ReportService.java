package com.github.tehsteel.tpunishments.web.report;

import com.github.tehsteel.tpunishments.web.report.model.ReportEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
public final class ReportService {

	@Autowired
	private final ReportRepository reportRepository;

	public void insertReport(final ReportEntity report) {
		if (reportRepository.findById(report.getReportId()).orElse(null) != null) {
			reportRepository.save(report);
			return;
		}

		reportRepository.insert(report);
	}

	public List<ReportEntity> getAllReports() {
		return reportRepository.findAll();
	}
}
