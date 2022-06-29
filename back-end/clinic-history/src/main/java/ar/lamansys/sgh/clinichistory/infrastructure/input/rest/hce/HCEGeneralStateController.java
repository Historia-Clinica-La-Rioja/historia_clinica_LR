package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEAllergyService;
import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEClinicalObservationService;
import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEHealthConditionsService;
import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEImmunizationService;
import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEMedicationService;
import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEToothRecordService;
import ar.lamansys.sgh.clinichistory.application.fetchSummaryClinicHistory.FetchSummaryClinicHistory;
import ar.lamansys.sgh.clinichistory.application.getactiveepisodemedicalcoverage.GetActiveEpisodeMedicalCoverage;
import ar.lamansys.sgh.clinichistory.application.getcriticalallergies.GetCriticalAllergies;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAllergyBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEHospitalizationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEMedicationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEPersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEToothRecordBo;
import ar.lamansys.sgh.clinichistory.domain.hce.Last2HCERiskFactorsBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.EvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationDoseBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEAllergyDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEAnthropometricDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEEvolutionSummaryDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEHospitalizationHistoryDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEImmunizationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCELast2RiskFactorsDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEMedicationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEPersonalHistoryDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEToothRecordDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.mapper.HCEGeneralStateMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.SharedImmunizationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineDoseInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/hce/general-state")
@Tag(name = "HCE General State", description = "HCE General State")
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA')")
public class HCEGeneralStateController {

    private static final Logger LOG = LoggerFactory.getLogger(HCEGeneralStateController.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> institutionId {}, patientId {}";

    private final HCEHealthConditionsService hceHealthConditionsService;

    private final HCEClinicalObservationService hceClinicalObservationService;

    private final HCEGeneralStateMapper hceGeneralStateMapper;

    private final HCEImmunizationService hceImmunizationService;

    private final HCEMedicationService hceMedicationService;

    private final HCEAllergyService hceAllergyService;

    private final HCEToothRecordService hceToothRecordService;

    private final LocalDateMapper localDateMapper;

    private final SharedImmunizationPort sharedImmunizationPort;

    private final SharedInstitutionPort sharedInstitutionPort;

    private final SharedStaffPort sharedStaffPort;

    private final FetchSummaryClinicHistory fetchSummaryClinicHistory;

	private final GetActiveEpisodeMedicalCoverage getActiveEpisodeMedicalCoverage;

	private final GetCriticalAllergies getCriticalAllergies;

    public HCEGeneralStateController(HCEHealthConditionsService hceHealthConditionsService,
                                     HCEClinicalObservationService hceClinicalObservationService,
                                     HCEGeneralStateMapper hceGeneralStateMapper,
                                     HCEImmunizationService hceImmunizationService,
                                     HCEMedicationService hceMedicationService,
                                     HCEAllergyService hceAllergyService,
                                     HCEToothRecordService hceToothRecordService,
                                     LocalDateMapper localDateMapper,
                                     SharedImmunizationPort sharedImmunizationPort,
                                     SharedInstitutionPort sharedInstitutionPort,
                                     SharedStaffPort sharedStaffPort, FetchSummaryClinicHistory fetchSummaryClinicHistory,
									 GetActiveEpisodeMedicalCoverage getActiveEpisodeMedicalCoverage,
									 GetCriticalAllergies getCriticalAllergies) {
        this.hceHealthConditionsService = hceHealthConditionsService;
        this.hceClinicalObservationService = hceClinicalObservationService;
        this.hceGeneralStateMapper = hceGeneralStateMapper;
        this.hceImmunizationService = hceImmunizationService;
        this.hceMedicationService = hceMedicationService;
        this.hceAllergyService = hceAllergyService;
        this.hceToothRecordService = hceToothRecordService;
        this.localDateMapper = localDateMapper;
        this.sharedImmunizationPort = sharedImmunizationPort;
        this.sharedInstitutionPort = sharedInstitutionPort;
        this.sharedStaffPort = sharedStaffPort;
        this.fetchSummaryClinicHistory = fetchSummaryClinicHistory;
		this.getActiveEpisodeMedicalCoverage = getActiveEpisodeMedicalCoverage;
    	this.getCriticalAllergies = getCriticalAllergies;
	}

    @GetMapping("/personalHistories")
    public ResponseEntity<List<HCEPersonalHistoryDto>> getPersonalHistories(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEPersonalHistoryBo> resultService = hceHealthConditionsService.getActivePersonalHistories(patientId);
        List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/familyHistories")
    public ResponseEntity<List<HCEPersonalHistoryDto>> getFamilyHistories(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEPersonalHistoryBo> resultService = hceHealthConditionsService.getFamilyHistories(patientId);
        List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/riskFactors")
    public ResponseEntity<HCELast2RiskFactorsDto> getRiskFactors(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        Last2HCERiskFactorsBo resultService = hceClinicalObservationService.getLast2RiskFactorsGeneralState(patientId);
        HCELast2RiskFactorsDto result = hceGeneralStateMapper.toHCELast2RiskFactorsDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/anthropometricData")
    public ResponseEntity<HCEAnthropometricDataDto> getAnthropometricData(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        HCEAnthropometricDataBo resultService = hceClinicalObservationService.getLastAnthropometricDataGeneralState(patientId);
        HCEAnthropometricDataDto result = hceGeneralStateMapper.toHCEAnthropometricDataDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

	@GetMapping("/last-2-anthropometric-data")
	public ResponseEntity<List<HCEAnthropometricDataDto>> getLast2AnthropometricData(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId) {
		LOG.debug(LOGGING_INPUT, institutionId, patientId);
		List<HCEAnthropometricDataBo> resultService = hceClinicalObservationService.getLast2AnthropometricDataGeneralState(patientId);
		List<HCEAnthropometricDataDto> result = hceGeneralStateMapper.toListHCEAnthropometricDataDto(resultService);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

    @GetMapping("/immunizations")
    public ResponseEntity<List<HCEImmunizationDto>> getImmunizations(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEImmunizationDto> result = hceImmunizationService.getImmunization(patientId)
                .stream().map(this::mapImmunizationResult)
                .collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    private HCEImmunizationDto mapImmunizationResult(HCEImmunizationBo hceImmunizationBo) {
        HCEImmunizationDto result = new HCEImmunizationDto();
        result.setId(hceImmunizationBo.getId());
        result.setSnomed(SnomedDto.from(hceImmunizationBo.getSnomed()));
        result.setStatusId(hceImmunizationBo.getStatusId());
        result.setAdministrationDate(localDateMapper.fromLocalDateToString(hceImmunizationBo.getAdministrationDate()));
        result.setCondition(hceImmunizationBo.getConditionId() != null ? sharedImmunizationPort.fetchVaccineConditionInfo(hceImmunizationBo.getConditionId()) : null);
        result.setScheme(hceImmunizationBo.getSchemeId() != null ? sharedImmunizationPort.fetchVaccineSchemeInfo(hceImmunizationBo.getSchemeId()) : null);
        result.setDose(mapVaccineDose(hceImmunizationBo.getDose()));
        result.setNote(hceImmunizationBo.getNote());
        result.setInstitution(mapInstitutionInfo(hceImmunizationBo));
        result.setLotNumber(hceImmunizationBo.getLotNumber());
        result.setDoctor(mapProfessionalInfoDto(hceImmunizationBo));
        return  result;
    }

    private ProfessionalInfoDto mapProfessionalInfoDto(HCEImmunizationBo hceImmunizationBo) {
        if (hceImmunizationBo.isBillable())
            return sharedStaffPort.getProfessionalCompleteInfo(hceImmunizationBo.getCreatedByUserId());
        return new ProfessionalInfoDto(null, null, hceImmunizationBo.getDoctorInfo(), null, null, null, null, null);
    }

    private InstitutionInfoDto mapInstitutionInfo(HCEImmunizationBo hceImmunizationBo) {
        if (hceImmunizationBo.getInstitutionId() == null && hceImmunizationBo.getInstitutionInfo() == null)
            return null;
        return hceImmunizationBo.getInstitutionId() != null ?
                sharedInstitutionPort.fetchInstitutionById(hceImmunizationBo.getInstitutionId()) :
                new InstitutionInfoDto(null, hceImmunizationBo.getInstitutionInfo(), null);
    }

    private VaccineDoseInfoDto mapVaccineDose(ImmunizationDoseBo dose) {
        if (dose == null)
            return null;
        return new VaccineDoseInfoDto(dose.getDescription(), dose.getOrder());
    }

    @GetMapping("/medications")
    public ResponseEntity<List<HCEMedicationDto>> getMedications(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEMedicationBo> resultService = hceMedicationService.getMedication(patientId);
        List<HCEMedicationDto> result = hceGeneralStateMapper.toListHCEMedicationDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/allergies")
    public ResponseEntity<List<HCEAllergyDto>> getAllergies(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEAllergyBo> resultService = hceAllergyService.getAllergies(patientId);
        List<HCEAllergyDto> result = hceGeneralStateMapper.toListHCEAllergyDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

	@GetMapping("/critical-allergies")
	public ResponseEntity<List<HCEAllergyDto>> getCriticalAllergies(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId) {
		LOG.debug(LOGGING_INPUT, institutionId, patientId);
		List<HCEAllergyDto> result = hceGeneralStateMapper.toListHCEAllergyDto(getCriticalAllergies.run(institutionId, patientId));
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

    @GetMapping("/chronic")
    public ResponseEntity<List<HCEPersonalHistoryDto>> getChronicConditions(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEPersonalHistoryBo> resultService = hceHealthConditionsService.getChronicConditions(patientId);
        List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/activeProblems")
    public ResponseEntity<List<HCEPersonalHistoryDto>> getActiveProblems(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
		List<HCEPersonalHistoryBo> activeProblems = hceHealthConditionsService.getActiveProblems(patientId);
		List<HCEPersonalHistoryBo> resultService = activeProblems;
        List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/solvedProblems")
    public ResponseEntity<List<HCEPersonalHistoryDto>> getSolvedProblems(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEPersonalHistoryBo> resultService = hceHealthConditionsService.getSolvedProblems(patientId);
        List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/toothRecords/tooth/{toothSctid}")
    public ResponseEntity<List<HCEToothRecordDto>> getToothRecords(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @PathVariable(name = "toothSctid") String toothSctid) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, toothSctid {}", institutionId, patientId, toothSctid);
        List<HCEToothRecordBo> resultService = hceToothRecordService.getToothRecords(patientId, toothSctid);
        List<HCEToothRecordDto> result = hceGeneralStateMapper.toListHCEToothRecordDto(resultService);
        LOG.debug("Output size -> {}", result.size());
        LOG.trace(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/hospitalization")
    public ResponseEntity<List<HCEHospitalizationHistoryDto>> getHospitalizationHistory(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEHospitalizationBo> resultService = hceHealthConditionsService.getHospitalizationHistory(patientId);
        List<HCEHospitalizationHistoryDto> result = groupHospitalizationsBySource(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    private List<HCEHospitalizationHistoryDto> groupHospitalizationsBySource(List<HCEHospitalizationBo> hospitalizationList){
            Map<Integer, List<HCEHospitalizationBo>> hospitalizationsBySource = hospitalizationList.stream()
                    .collect(Collectors.groupingBy(h -> h.getSourceId()));
            List<HCEHospitalizationHistoryDto> hospitalizationsGrouped = hospitalizationsBySource.entrySet().stream()
                    .map(hg ->
                            new HCEHospitalizationHistoryDto(hg.getKey(),
                                    localDateMapper.fromLocalDateToString(hg.getValue().get(0).getEntryDate()),
                                    localDateMapper.fromLocalDateToString(hg.getValue().get(0).getDischargeDate()),
                                    hg.getValue().stream().filter(HCEHospitalizationBo::isMain).map(hceGeneralStateMapper::toHCEDiagnoseDto).collect(Collectors.toList()),
                                    hg.getValue().stream().filter(hbo -> !hbo.isMain()).map(hceGeneralStateMapper::toHCEDiagnoseDto).collect(Collectors.toList())))
                    .collect(Collectors.toList());
            LOG.debug(LOGGING_OUTPUT, hospitalizationsGrouped);
            return hospitalizationsGrouped.stream().sorted(Comparator.comparing(HCEHospitalizationHistoryDto::getEntryDate).reversed()
                    .thenComparing(HCEHospitalizationHistoryDto::getDischargeDate, Comparator.nullsFirst(Comparator.reverseOrder()))).collect(Collectors.toList());
    }



    @GetMapping("/summary-list")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA')")
    public ResponseEntity<List<HCEEvolutionSummaryDto>> getEvolutionSummaryList(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId){
        List<EvolutionSummaryBo> evolutions = fetchSummaryClinicHistory.run(patientId);
        List<HCEEvolutionSummaryDto> result = hceGeneralStateMapper.fromListOutpatientEvolutionSummaryBo(evolutions);
        LOG.debug("Get summary  => {}", result);
        return ResponseEntity.ok(result);
    }

	@GetMapping("/active-internment-episode/{internmentEpisodeId}/medical-coverage")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<ExternalPatientCoverageDto> getActiveEpisodeMedicalCoverage(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		ExternalPatientCoverageDto result = getActiveEpisodeMedicalCoverage.run(internmentEpisodeId);
		LOG.debug("Get active internment episode medical coverage => {}", result);
		return ResponseEntity.ok(result);
	}

}