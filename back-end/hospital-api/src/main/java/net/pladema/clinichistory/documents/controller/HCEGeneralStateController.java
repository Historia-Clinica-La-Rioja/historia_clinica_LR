package net.pladema.clinichistory.documents.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.controller.dto.HCEAllergyDto;
import net.pladema.clinichistory.documents.controller.dto.HCEAnthropometricDataDto;
import net.pladema.clinichistory.documents.controller.dto.HCEHospitalizationHistoryDto;
import net.pladema.clinichistory.documents.controller.dto.HCEImmunizationDto;
import net.pladema.clinichistory.documents.controller.dto.HCELast2VitalSignsDto;
import net.pladema.clinichistory.documents.controller.dto.HCEMedicationDto;
import net.pladema.clinichistory.documents.controller.dto.HCEPersonalHistoryDto;
import net.pladema.clinichistory.documents.controller.mapper.HCEGeneralStateMapper;
import net.pladema.clinichistory.documents.service.hce.HCEAllergyService;
import net.pladema.clinichistory.documents.service.hce.HCEClinicalObservationService;
import net.pladema.clinichistory.documents.service.hce.HCEHealthConditionsService;
import net.pladema.clinichistory.documents.service.hce.HCEImmunizationService;
import net.pladema.clinichistory.documents.service.hce.HCEMedicationService;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAllergyBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEHospitalizationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEMedicationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEPersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.Last2HCEVitalSignsBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/hce/general-state")
@Api(value = "HCE General State", tags = { "HCE General State" })
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD')")
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

    private final LocalDateMapper localDateMapper;

    public HCEGeneralStateController(HCEHealthConditionsService hceHealthConditionsService,
                                     HCEClinicalObservationService hceClinicalObservationService,
                                     HCEGeneralStateMapper hceGeneralStateMapper,
                                     HCEImmunizationService hceImmunizationService,
                                     HCEMedicationService hceMedicationService,
                                     HCEAllergyService hceAllergyService,
                                     LocalDateMapper localDateMapper) {
        this.hceHealthConditionsService = hceHealthConditionsService;
        this.hceClinicalObservationService = hceClinicalObservationService;
        this.hceGeneralStateMapper = hceGeneralStateMapper;
        this.hceImmunizationService = hceImmunizationService;
        this.hceMedicationService = hceMedicationService;
        this.hceAllergyService = hceAllergyService;
        this.localDateMapper = localDateMapper;
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

    @GetMapping("/vitalSigns")
    public ResponseEntity<HCELast2VitalSignsDto> getVitalSigns(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        Last2HCEVitalSignsBo resultService = hceClinicalObservationService.getLast2VitalSignsGeneralState(patientId);
        HCELast2VitalSignsDto result = hceGeneralStateMapper.toHCELast2VitalSignsDto(resultService);
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

    @GetMapping("/immunizations")
    public ResponseEntity<List<HCEImmunizationDto>> getImmunizations(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEImmunizationBo> resultService = hceImmunizationService.getImmunization(patientId);
        List<HCEImmunizationDto> result = hceGeneralStateMapper.toListHCEImmunizationDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
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
        List<HCEPersonalHistoryBo> resultService = hceHealthConditionsService.getActiveProblems(patientId);
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


}