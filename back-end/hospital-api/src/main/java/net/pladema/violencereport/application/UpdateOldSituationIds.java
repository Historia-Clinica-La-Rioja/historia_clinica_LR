package net.pladema.violencereport.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportSituationHistoryRepository;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReportSituationHistory;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class UpdateOldSituationIds {

	private ViolenceReportRepository violenceReportRepository;

	private ViolenceReportSituationHistoryRepository violenceReportSituationHistoryRepository;

	public void run(Integer patientId) {
		log.debug("Input parameters -> patientId {}", patientId);
		List<ViolenceReportSituationHistory> history = violenceReportSituationHistoryRepository.getAllByPatientId(patientId);
		history.forEach(this::updateSituationIds);
	}

	private void updateSituationIds(ViolenceReportSituationHistory element) {
		violenceReportRepository.updateViolenceReportSituationId(element.getReportId(), element.getOldSituationId());
		violenceReportSituationHistoryRepository.deleteById(element.getReportId());
	}

}
