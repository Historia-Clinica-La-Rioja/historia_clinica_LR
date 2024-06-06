package net.pladema.violencereport.application;

import ar.lamansys.sgx.shared.files.pdf.CreatePDFDocument;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.violencereport.domain.ViolenceReportBo;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class GeneratePDFDocument {

	private GetSituationEvolution getSituationEvolution;

	private GenerateContext generateContext;

	private CreatePDFDocument createPDFDocument;

	public StoredFileBo run(Integer patientId, Short situationId, Short evolutionId) {
		log.debug("Input parameters -> patientId {}, situationId {}, evolutionId {}", patientId, situationId, evolutionId);
		final String TEMPLATE_NAME = "violence_report";
		ViolenceReportBo report = getSituationEvolution.run(patientId, situationId, evolutionId);
		StoredFileBo result = createPDFDocument.run(TEMPLATE_NAME, generateFileName(report.getPatientId()), report, generateContext);
		log.debug("Output -> {}", result);
		return result;
	}

	private String generateFileName(Integer patientId) {
		final String FILE_NAME = "REPORTE_VIOLENCIA_PACIENTE_";
		return FILE_NAME + patientId;
	}

}
