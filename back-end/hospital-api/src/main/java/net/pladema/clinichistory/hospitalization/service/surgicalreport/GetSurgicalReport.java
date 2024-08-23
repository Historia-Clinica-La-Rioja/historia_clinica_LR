package net.pladema.clinichistory.hospitalization.service.surgicalreport;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SurgicalReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProcedureTypeEnum;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.service.domain.ProsthesisInfoBo;
import net.pladema.clinichistory.hospitalization.service.domain.SurgicalReportBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetSurgicalReport {

	private final DocumentService documentService;

	private final SurgicalReportRepository surgicalReportRepository;

	public SurgicalReportBo run(Long documentId){
		log.debug("Input parameter -> documentId {}", documentId);
		SurgicalReportBo result = new SurgicalReportBo();
		Document document = documentService.findById(documentId)
				.orElseThrow(()-> new NotFoundException("surgical-report-not-found", String.format("El parte quirurgico con id %s no existe", documentId)));
		result.setId(documentId);
		result.setPatientId(document.getPatientId());
		result.setEncounterId(document.getSourceId());
		result.setInstitutionId(document.getInstitutionId());
		GeneralHealthConditionBo generalHealthConditionBo = documentService.getHealthConditionFromDocument(documentId);
		result.setMainDiagnosis(new DiagnosisBo(generalHealthConditionBo.getMainDiagnosis()));
		result.setPreoperativeDiagnosis(generalHealthConditionBo.getPreoperativeDiagnosis());
		result.setPostoperativeDiagnosis(generalHealthConditionBo.getPostoperativeDiagnosis());
		List<ProcedureBo> procedures = documentService.getProcedureStateFromDocument(documentId);
		result.setProcedures(filterProceduresByType(procedures, ProcedureTypeEnum.PROCEDURE));
		result.setAnesthesia(filterProceduresByType(procedures, ProcedureTypeEnum.ANESTHESIA_PROCEDURE));
		result.setSurgeryProcedures(filterProceduresByType(procedures, ProcedureTypeEnum.SURGICAL_PROCEDURE));
		result.setCultures(filterProceduresByType(procedures, ProcedureTypeEnum.CULTURE));
		result.setFrozenSectionBiopsies(filterProceduresByType(procedures, ProcedureTypeEnum.FROZEN_SECTION_BIOPSY));
		result.setDrainages(filterProceduresByType(procedures, ProcedureTypeEnum.DRAINAGE));
		result.setHealthcareProfessionals(documentService.getHealthcareProfessionalsFromDocument(documentId));
		result.setProsthesisInfo(getProsthesisInfoByDocumentId(documentId));
		result.setStartDateTime(surgicalReportRepository.getStartDateTime(documentId));
		result.setEndDateTime(surgicalReportRepository.getEndDateTime(documentId));
		result.setDescription((surgicalReportRepository.getDescription(documentId)));
		log.debug("Output result -> {}", result);
		return result;
	}

	private ProsthesisInfoBo getProsthesisInfoByDocumentId(Long documentId) {
		Boolean hasProsthesis = surgicalReportRepository.getHasProsthesis(documentId);
		if (Objects.isNull(hasProsthesis)) {
			return null;
		}
		return new ProsthesisInfoBo(
				hasProsthesis,
				documentService.getProsthesisDescriptionFromDocument(documentId).orElse(null)
		);

	}

	private List<ProcedureBo> filterProceduresByType(List<ProcedureBo> procedures, ProcedureTypeEnum type){
		return procedures.stream().filter(p -> p.getType().equals(type)).collect(Collectors.toList());
	}


}
