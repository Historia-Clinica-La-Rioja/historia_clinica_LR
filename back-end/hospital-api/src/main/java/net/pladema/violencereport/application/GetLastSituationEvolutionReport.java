package net.pladema.violencereport.application;

import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.violencereport.domain.ViolenceReportBo;

import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class GetLastSituationEvolutionReport {

	private ViolenceReportRepository violenceReportRepository;

	private GetEditViolenceReportById getEditViolenceReportById;

	public ViolenceReportBo run(Integer patientId, Integer situationId) {
		log.debug("Input parameters -> patientId {}, situationId {}", patientId, situationId);
		Integer reportId = violenceReportRepository.getLastSituationEvolutionReportId(patientId, situationId);
		assertNonNullReportId(reportId);
		ViolenceReportBo result = getEditViolenceReportById.run(reportId);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertNonNullReportId(Integer reportId) {
		Assert.notNull(reportId, "No se puede evolucionar un episodio inexistente");
	}

}
