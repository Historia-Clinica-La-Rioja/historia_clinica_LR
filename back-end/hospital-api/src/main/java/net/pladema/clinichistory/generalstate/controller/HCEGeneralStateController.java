package net.pladema.clinichistory.generalstate.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.generalstate.controller.dto.*;
import net.pladema.clinichistory.generalstate.controller.mapper.HCEGeneralStateMapper;
import net.pladema.clinichistory.generalstate.service.*;
import net.pladema.clinichistory.generalstate.service.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/hce/general-state")
@Api(value = "HCE General State", tags = { "HCE General State" })
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD')")
public class HCEGeneralStateController {

    private static final Logger LOG = LoggerFactory.getLogger(HCEGeneralStateController.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> institutionId {}, patientId {}";

    private final HCEGeneralStateService hceGeneralStateService;

    private final HCEClinicalObsGeneralStateService hceClinicalObsGeneralStateService;

    private final HCEGeneralStateMapper hceGeneralStateMapper;

    private final HCEInmunizationService hceInmunizationService;

    private final HCEMedicationService hceMedicationService;

    private final HCEAllergyService hceAllergyService;

    public HCEGeneralStateController(HCEGeneralStateService hceGeneralStateService,
                                     HCEClinicalObsGeneralStateService hceClinicalObsGeneralStateService,
                                     HCEGeneralStateMapper hceGeneralStateMapper, HCEInmunizationService hceInmunizationService, HCEMedicationService hceMedicationService, HCEAllergyService hceAllergyService) {
        this.hceGeneralStateService = hceGeneralStateService;
        this.hceClinicalObsGeneralStateService = hceClinicalObsGeneralStateService;
        this.hceGeneralStateMapper = hceGeneralStateMapper;
        this.hceInmunizationService = hceInmunizationService;
        this.hceMedicationService = hceMedicationService;
        this.hceAllergyService = hceAllergyService;
    }

    @GetMapping("/personalHistory")
    public ResponseEntity<List<HCEPersonalHistoryDto>> getPersonalHistory(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEPersonalHistoryBo> resultService = hceGeneralStateService.getPersonalHistory(patientId);
        List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/familyHistory")
    public ResponseEntity<List<HCEPersonalHistoryDto>> getFamilyHistory(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEPersonalHistoryBo> resultService = hceGeneralStateService.getFamilyHistory(patientId);
        List<HCEPersonalHistoryDto> result = hceGeneralStateMapper.toListHCEPersonalHistoryDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/vitalSigns")
    public ResponseEntity<HCELast2VitalSignsDto> getVitalSigns(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        Last2HCEVitalSignsBo resultService = hceClinicalObsGeneralStateService.getLast2VitalSignsGeneralState(patientId);
        HCELast2VitalSignsDto result = hceGeneralStateMapper.toHCELast2VitalSignsDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/anthropometricData")
    public ResponseEntity<HCEAnthropometricDataDto> getAnthropometricData(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        HCEAnthropometricDataBo resultService = hceClinicalObsGeneralStateService.getLastAnthropometricDataGeneralState(patientId);
        HCEAnthropometricDataDto result = hceGeneralStateMapper.toHCEAnthropometricDataDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/inmunization")
    public ResponseEntity<List<HCEInmunizationDto>> getInmunization(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEInmunizationBo> resultService = hceInmunizationService.getInmunization(patientId);
        List<HCEInmunizationDto> result = hceGeneralStateMapper.toListHCEInmunizationDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/medication")
    public ResponseEntity<List<HCEMedicationDto>> getMedication(
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
}