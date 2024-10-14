package net.pladema.reports.infrastructure.output.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.pladema.reports.infrastructure.output.ReportQueueStorage;
import net.pladema.reports.infrastructure.output.repository.ReportQueueRepository;
import net.pladema.reports.infrastructure.output.repository.entity.ReportQueue;

@AllArgsConstructor
@Service
public class ReportQueueStorageImpl implements ReportQueueStorage {

	private final ReportQueueRepository repository;

	@Override
	public Optional<ReportQueue> findFirstToProcess() {
		var reportToProcessPage = repository.findToProcessByAge(Pageable.ofSize(1));
		return reportToProcessPage.stream().findFirst();
	}

	@Override
	public void generated(ReportQueue report, Long fileId) {
		report.setFileId(fileId);
		report.setGeneratedError(null);
		saveReportInstitutionMonthlyQueue(report);
	}

	@Override
	public void generated(ReportQueue report, String message) {
		report.setGeneratedError(message);
		saveReportInstitutionMonthlyQueue(report);
	}

	private void saveReportInstitutionMonthlyQueue(ReportQueue report) {
		// siempre que pasó por el proceso de descarga (mal o bien) se guarda la hora
		report.setGeneratedOn(LocalDateTime.now());
		repository.save(report);
		repository.flush();
	}

	@Override
	public void generating(ReportQueue report) {
		report.setGeneratedError("...");
		repository.save(report);
	}
}
