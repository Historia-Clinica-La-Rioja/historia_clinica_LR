package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DentalActionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.SharedImmunizationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineDoseInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;

@Service
public class AuditableContextBuilder {

	private final Logger logger;
	private final Function<Integer, BasicPatientDto> basicDataFromPatientLoader;
	private final Function<Long, ProfessionalCompleteDto> authorFromDocumentFunction;
	private final Function<Integer, ClinicalSpecialtyDto> clinicalSpecialtyDtoFunction;
	private final SharedImmunizationPort sharedImmunizationPort;
	private final RiskFactorMapper riskFactorMapper;
	private final LocalDateMapper localDateMapper;
	private final FeatureFlagsService featureFlagsService;

	public AuditableContextBuilder(
			SharedPatientPort sharedPatientPort,
			DocumentAuthorFinder documentAuthorFinder,
			ClinicalSpecialtyFinder clinicalSpecialtyFinder,
			SharedImmunizationPort sharedImmunizationPort,
			RiskFactorMapper riskFactorMapper,
			LocalDateMapper localDateMapper,
			FeatureFlagsService featureFlagsService) {
		this.sharedImmunizationPort = sharedImmunizationPort;
		this.localDateMapper = localDateMapper;
		this.logger = LoggerFactory.getLogger(getClass());
		this.basicDataFromPatientLoader = sharedPatientPort::getBasicDataFromPatient;
		this.authorFromDocumentFunction = (Long documentId) -> documentAuthorFinder.getAuthor(documentId);
		this.clinicalSpecialtyDtoFunction = (Integer specialtyId) ->
				clinicalSpecialtyFinder.getClinicalSpecialty(specialtyId);
		this.riskFactorMapper = riskFactorMapper;
		this.featureFlagsService = featureFlagsService;
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
		contextMap.put("dentalDiagnostics", document.getDentalActions().stream().filter(DentalActionBo::isDiagnostic).collect(Collectors.toList()));
		contextMap.put("dentalProcedures", document.getDentalActions().stream().filter(DentalActionBo::isProcedure).collect(Collectors.toList()));

		var immunizations =  mapImmunizations(document.getImmunizations());
		contextMap.put("billableImmunizations", immunizations.stream().filter(ImmunizationInfoDto::isBillable).collect(Collectors.toList()));
		contextMap.put("nonBillableImmunizations", immunizations.stream().filter(i -> !i.isBillable()).collect(Collectors.toList()));

		contextMap.put("medications", document.getMedications());
		contextMap.put("anthropometricData", document.getAnthropometricData());
		contextMap.put("riskFactors", riskFactorMapper.toRiskFactorsReportDto(document.getRiskFactors()));
		contextMap.put("notes", document.getNotes());
		contextMap.put("author", authorFromDocumentFunction.apply(document.getId()));
		contextMap.put("clinicalSpecialty", clinicalSpecialtyDtoFunction.apply(document.getClinicalSpecialtyId()));
		contextMap.put("performedDate", document.getPerformedDate().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")));
		contextMap.put("nameSelfDeterminationFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
	}

	private List<ImmunizationInfoDto> mapImmunizations(List<ImmunizationBo> immunizations) {
		return immunizations.stream().map(this::mapToImmunizationInfoDto).collect(Collectors.toList());
	}

	private ImmunizationInfoDto mapToImmunizationInfoDto(ImmunizationBo immunizationBo) {
		ImmunizationInfoDto result = new ImmunizationInfoDto();
		result.setId(immunizationBo.getId());
		result.setBillable(immunizationBo.isBillable());
		result.setLotNumber(immunizationBo.getLotNumber());
		result.setSnomed(new SnomedDto(immunizationBo.getSnomed().getSctid(), immunizationBo.getSnomed().getPt()));
		result.setAdministrationDate(localDateMapper.fromLocalDateToString(immunizationBo.getAdministrationDate()));
		result.setInstitutionInfo(immunizationBo.getInstitutionInfo());
		result.setDoctorInfo(immunizationBo.getDoctorInfo());
		result.setNote(immunizationBo.getNote());
		result.setCondition(immunizationBo.getConditionId() != null ?
				sharedImmunizationPort.fetchVaccineConditionInfo(immunizationBo.getConditionId()) : null);
		result.setScheme(immunizationBo.getSchemeId() != null ?
				sharedImmunizationPort.fetchVaccineSchemeInfo(immunizationBo.getSchemeId()) : null);
		result.setDose(immunizationBo.getDose() != null ?
						new VaccineDoseInfoDto(immunizationBo.getDose().getDescription(), immunizationBo.getDose().getOrder()) : null);
		return result;
	}


}

