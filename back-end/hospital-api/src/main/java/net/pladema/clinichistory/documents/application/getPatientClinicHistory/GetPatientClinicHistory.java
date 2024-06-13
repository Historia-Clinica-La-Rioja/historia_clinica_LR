package net.pladema.clinichistory.documents.application.getPatientClinicHistory;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import net.pladema.clinichistory.documents.application.ClinicHistoryStorage;
import net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo;
import net.pladema.clinichistory.documents.domain.CHSearchFilterBo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GetPatientClinicHistory {

	private static final List<Short> UNDEFINED_DOCUMENT_TYPES =
			List.of(DocumentType.MEDICAL_IMAGE_REPORT, DocumentType.SURGICAL_HOSPITALIZATION_REPORT, DocumentType.SURGICAL_HOSPITALIZATION_REPORT, DocumentType.ANESTHETIC_REPORT);

	private final ClinicHistoryStorage clinicHistoryStorage;

	public GetPatientClinicHistory(ClinicHistoryStorage clinicHistoryStorage){
		this.clinicHistoryStorage = clinicHistoryStorage;
	}

	public List<CHDocumentSummaryBo> run (Integer patientId, LocalDate from, LocalDate to, CHSearchFilterBo filter){
		log.debug("Input parameters -> patientId {}, from{}, to{}, filter{}", patientId, from, to, filter);
		var result = clinicHistoryStorage.getPatientClinicHistory(patientId, from, to)
				.stream()
				.filter(o -> filter.getEncounterTypeList().contains(o.getEncounterType())
						&& (filter.getDocumentTypeList().isEmpty() || filter.getDocumentTypeList().contains(o.getDocumentType()))
						&& !UNDEFINED_DOCUMENT_TYPES.contains(o.getTypeId()))
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

}
