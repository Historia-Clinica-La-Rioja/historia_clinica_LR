package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.VitalSignMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;

@Service
public class AuditableContextBuilder {

	private final Logger logger;
	private final Function<Integer, BasicPatientDto> basicDataFromPatientLoader;
	private final Function<Long, AuthorDto> authorFromDocumentFunction;
	private final Function<Integer, ClinicalSpecialtyDto> clinicalSpecialtyDtoFunction;
	private final VitalSignMapper vitalSignMapper;

	public AuditableContextBuilder(
			SharedPatientPort sharedPatientPort,
			DocumentAuthorFinder documentAuthorFinder,
			ClinicalSpecialtyFinder clinicalSpecialtyFinder,
			AuthorMapper authorMapper,
			VitalSignMapper vitalSignMapper
	) {
		this.logger = LoggerFactory.getLogger(getClass());
		this.basicDataFromPatientLoader = sharedPatientPort::getBasicDataFromPatient;
		this.authorFromDocumentFunction = (Long documentId) -> authorMapper.toAuthorDto(
				documentAuthorFinder.getAuthor(documentId)
		);
		this.clinicalSpecialtyDtoFunction = (Integer specialtyId) ->
				clinicalSpecialtyFinder.getClinicalSpecialty(specialtyId);
		this.vitalSignMapper = vitalSignMapper;
	}

	public <T extends IDocumentBo> Map<String,Object> buildContext(T document, Integer patientId){
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
	private <T extends IDocumentBo> void addDocumentInfo(Map<String,Object> contextMap, T document) {
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
		contextMap.put("author", authorFromDocumentFunction.apply(document.getId()));
		contextMap.put("clinicalSpecialty", clinicalSpecialtyDtoFunction.apply(document.getClinicalSpecialtyId()));
	}


}
