package net.pladema.violencereport.application;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.violencereport.domain.ViolenceReportBo;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class SaveNewViolenceEpisode {

	private ViolenceReportRepository violenceReportRepository;

	private SaveViolenceReport saveViolenceReport;

	public Integer run(ViolenceReportBo violenceReport) {
		log.debug("Input parameter -> violenceReport {}", violenceReport);
		final Short FIRST_EVOLUTION_ID = 0;
		setSituationId(violenceReport);
		violenceReport.setEvolutionId(FIRST_EVOLUTION_ID);
		Integer result = saveViolenceReport.run(violenceReport);
		log.debug("Output -> {}", result);
		return result;
	}

	private void setSituationId(ViolenceReportBo violenceReport) {
		Integer patientLastSituationId = violenceReportRepository.getPatientLastSituationId(violenceReport.getPatientId());
		if (patientLastSituationId == null)
			violenceReport.setSituationId((short) 1);
		else
			violenceReport.setSituationId((short) (patientLastSituationId + 1));
	}

}
