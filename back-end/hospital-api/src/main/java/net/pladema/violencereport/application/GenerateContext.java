package net.pladema.violencereport.application;

import java.util.HashMap;
import java.util.Map;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgx.shared.files.pdf.GenerateDocumentContext;
import net.pladema.patient.application.GetDocumentPatient;
import net.pladema.patient.domain.DocumentPatientBo;
import net.pladema.violencereport.domain.ViolenceReportBo;
import net.pladema.violencereport.infrastructure.input.rest.mapper.ViolenceReportMapper;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportDto;

@Slf4j
@AllArgsConstructor
@Service
public class GenerateContext implements GenerateDocumentContext<ViolenceReportBo> {

	private ViolenceReportMapper violenceReportMapper;

	private GetDocumentPatient getDocumentPatient;

	private SharedPersonPort sharedPersonPort;

	@Override
	public Map<String, Object> run(ViolenceReportBo report) {
		log.debug("Input parameters -> report {}", report);
		ViolenceReportDto parsedReport = violenceReportMapper.toViolenceReportDto(report);
		Map<String, Object> result = generateViolenceReportContext(parsedReport, report);
		log.debug("Output -> {}", result);
		return result;
	}

	private Map<String, Object> generateViolenceReportContext(ViolenceReportDto report, ViolenceReportBo reportBo) {
		Map<String, Object> result = new HashMap<>();
		result.put("victimData", report.getVictimData());
		result.put("episodeData", report.getEpisodeData());
		result.put("aggressors", report.getAggressorData());
		result.put("implementedActions", report.getImplementedActions());
		result.put("observations", report.getObservation());
		result.put("title", getTitle(reportBo));

		DocumentPatientBo patient = getDocumentPatient.run(reportBo.getPatientId());
		result.put("patientCompleteName", sharedPersonPort.parseCompletePersonName(patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getOtherLastName(), patient.getSelfPerceivedName()));
		result.put("patientData", patient);
		return result;
	}

	private String getTitle(ViolenceReportBo report) {
		String evolution = report.getEvolutionId() == 0 ? "Inicio de abordaje" : "Evolución " + report.getEvolutionId();
		return "Situación # " + report.getSituationId() + " | " + evolution;
	}

}
