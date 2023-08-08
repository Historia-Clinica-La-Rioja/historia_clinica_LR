package net.pladema.clinichistory.hospitalization.service.surgicalreport;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProcedureTypeEnum;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.service.domain.SurgicalReportBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetSurgicalReport {

	private final DocumentService documentService;

	public SurgicalReportBo run(Long surgicalReportId){
		log.debug("Input parameter -> surgicalReportId {}", surgicalReportId);
		SurgicalReportBo result = new SurgicalReportBo();
		Document document = documentService.findById(surgicalReportId)
				.orElseThrow(()-> new NotFoundException("surgical-report-not-found", String.format("El parte quirurgico con id %s no existe", surgicalReportId)));
		Long documentId = document.getId();
		result.setId(documentId);
		result.setPatientId(document.getPatientId());
		result.setEncounterId(document.getSourceId());
		result.setInstitutionId(document.getInstitutionId());
		GeneralHealthConditionBo generalHealthConditionBo = documentService.getHealthConditionFromDocument(documentId);
		result.setPreoperativeDiagnosis(generalHealthConditionBo.getPreoperativeDiagnosis());
		result.setPostoperativeDiagnosis(generalHealthConditionBo.getPostoperativeDiagnosis());
		List<ProcedureBo> procedures = documentService.getProcedureStateFromDocument(documentId);
		result.setProcedures(filterProceduresByType(procedures, ProcedureTypeEnum.PROCEDURE));
		result.setAnesthesia(filterProceduresByType(procedures, ProcedureTypeEnum.ANESTHESIA_PROCEDURE));
		result.setSurgeryProcedures(filterProceduresByType(procedures, ProcedureTypeEnum.SURGICAL_PROCEDURE));
		result.setCulture(filterProceduresByType(procedures, ProcedureTypeEnum.CULTURE).stream().findFirst().orElse(null));
		result.setFrozenSectionBiopsy(filterProceduresByType(procedures, ProcedureTypeEnum.FROZEN_SECTION_BIOPSY).stream().findFirst().orElse(null));
		result.setDrainage(filterProceduresByType(procedures, ProcedureTypeEnum.DRAINAGE).stream().findFirst().orElse(null));
		result.setHealthcareProfessionals(documentService.getHealthcareProfessionalsFromDocument(documentId));
		result.setProsthesisDescription(documentService.getProsthesisDescriptionFromDocument(documentId).orElse(null));
		log.debug("Output result -> {}", result);
		return result;
	}

	private List<ProcedureBo> filterProceduresByType(List<ProcedureBo> procedures, ProcedureTypeEnum type){
		return procedures.stream().filter(p -> p.getType().equals(type)).collect(Collectors.toList());
	}


}
