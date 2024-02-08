package net.pladema.patient.application.mergepatient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.patient.application.port.MergeClinicHistoryStorage;

import net.pladema.violencereport.domain.ViolenceReportBo;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportSituationHistoryRepository;

import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReportSituationHistory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class MigrateViolenceReport {

	private final MergeClinicHistoryStorage mergeClinicHistoryStorage;

	private ViolenceReportRepository violenceReportRepository;

	private ViolenceReportSituationHistoryRepository violenceReportSituationHistoryRepository;

	public void execute(List<Integer> oldPatientIds, Integer newPatientId) {
		log.debug("Input parameters -> oldPatientIds{}, newPatientId{}", oldPatientIds, newPatientId);
		Integer lastPatientSituationId = violenceReportRepository.getPatientLastSituationId(newPatientId);
		if (lastPatientSituationId != null)
			migrateViolenceReports(oldPatientIds, newPatientId, lastPatientSituationId);
	}

	private void migrateViolenceReports(List<Integer> oldPatientIds, Integer newPatientId, Integer lastPatientSituationId) {
		AtomicInteger newSituationId = new AtomicInteger(lastPatientSituationId + 1);
		oldPatientIds.forEach(oldPatientId -> saveAndUpdateSituationIds(oldPatientId, newSituationId));
		mergeClinicHistoryStorage.modifyViolenceReport(oldPatientIds, newPatientId);
	}

	private void saveAndUpdateSituationIds(Integer oldPatientId, AtomicInteger newSituationId) {
		List<ViolenceReportBo> oldSituationsWithEvolutions = violenceReportRepository.getAllPatientsSituationIds(List.of(oldPatientId));
		Set<Short> situationIds = oldSituationsWithEvolutions.stream().map(ViolenceReportBo::getSituationId).collect(Collectors.toSet());
		situationIds.forEach(oldSituationId -> processSituations(newSituationId, oldSituationId, oldSituationsWithEvolutions));
	}

	private void processSituations(AtomicInteger newSituationId, Short oldSituationId, List<ViolenceReportBo> oldSituationsWithEvolutions) {
		oldSituationsWithEvolutions.stream().filter(oldSituationWithEvolution -> oldSituationWithEvolution.getSituationId().equals(oldSituationId))
				.forEach(oldSituationWithEvolution -> savePreviousSituationId(oldSituationWithEvolution, newSituationId));
		newSituationId.getAndIncrement();
	}

	private void savePreviousSituationId(ViolenceReportBo oldSituationsWithEvolutions, AtomicInteger newSituationId) {
		violenceReportSituationHistoryRepository.save(new ViolenceReportSituationHistory(oldSituationsWithEvolutions.getId(), oldSituationsWithEvolutions.getSituationId()));
		violenceReportRepository.updateViolenceReportSituationId(oldSituationsWithEvolutions.getId(), newSituationId.shortValue());
	}

}
