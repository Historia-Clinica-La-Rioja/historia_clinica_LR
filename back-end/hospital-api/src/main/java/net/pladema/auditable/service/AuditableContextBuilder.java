package net.pladema.auditable.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.pladema.clinichistory.hospitalization.controller.dto.ResponsibleDoctorDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.ResponsibleDoctorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.documents.service.ReportDocumentService;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.VitalSignMapper;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.controller.service.PatientExternalService;

@Service
public class AuditableContextBuilder {

	private final Logger logger;
	private final Function<Integer, BasicPatientDto> basicDataFromPatientLoader;
	private final Function<Long, ResponsibleDoctorDto> responsibleDoctorFromDocumentLoader;
	private final VitalSignMapper vitalSignMapper;

	public AuditableContextBuilder(
			PatientExternalService patientExternalService,
			ReportDocumentService reportDocumentService,
			ResponsibleDoctorMapper responsibleDoctorMapper,
			VitalSignMapper vitalSignMapper
	) {
		this.logger = LoggerFactory.getLogger(getClass());
		this.basicDataFromPatientLoader = patientExternalService::getBasicDataFromPatient;
		this.responsibleDoctorFromDocumentLoader = (Long documentId) -> responsibleDoctorMapper.toResponsibleDoctorDto(
				reportDocumentService.getAuthor(documentId)
		);
		this.vitalSignMapper = vitalSignMapper;
	}

	public <T extends Document> Map<String,Object> buildContext(T document, Integer patientId){
		logger.debug("Input parameters -> document {}", document);

		Map<String,Object> contextMap = new HashMap<>();
		addPatientInfo(contextMap, patientId);
		addDocumentInfo(contextMap, document);
		logger.debug("Built context for patient {} and document {} is {}", patientId, document.getId(), contextMap);
		return contextMap;
	}
	private void addPatientInfo(Map<String,Object> contextMap, Integer patientId) {
		contextMap.put("patient", basicDataFromPatientLoader.apply(patientId));
	}
	private <T extends Document> void addDocumentInfo(Map<String,Object> contextMap, T document) {
		contextMap.put("mainDiagnosis", document.getMainDiagnosis());
		contextMap.put("diagnosis", document.getDiagnosis());
		contextMap.put("reasons", document.getReasons());
		contextMap.put("procedures", document.getProcedures());
		contextMap.put("problems", document.getProblems());
		contextMap.put("personalHistories", document.getPersonalHistories());
		contextMap.put("familyHistories", document.getFamilyHistories());
		contextMap.put("allergies", document.getAllergies());
		contextMap.put("immunizations", document.getImmunizations());
		contextMap.put("medications", document.getMedications());
		contextMap.put("anthropometricData", document.getAnthropometricData());
		contextMap.put("vitalSigns", vitalSignMapper.toVitalSignsReportDto(document.getVitalSigns()));
		contextMap.put("notes", document.getNotes());
		contextMap.put("author", responsibleDoctorFromDocumentLoader.apply(document.getId()));
		contextMap.put("clinicalSpecialty",document.getClinicalSpecialty());
	}


}
