package net.pladema.clinichistory.generalstate.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.generalstate.controller.dto.HCEAnthropometricDataDto;
import net.pladema.clinichistory.generalstate.controller.dto.HCEInmunizationDto;
import net.pladema.clinichistory.generalstate.controller.dto.HCELast2VitalSignsDto;
import net.pladema.clinichistory.generalstate.controller.dto.HCEPersonalHistoryDto;
import net.pladema.clinichistory.generalstate.controller.mapper.HCEGeneralStateMapper;
import net.pladema.clinichistory.generalstate.controller.mapper.HCEInmunizationMapper;
import net.pladema.clinichistory.generalstate.service.HCEClinicalObsGeneralStateService;
import net.pladema.clinichistory.generalstate.service.HCEGeneralStateService;
import net.pladema.clinichistory.generalstate.service.HCEInmunizationService;
import net.pladema.clinichistory.generalstate.service.domain.HCEAnthropometricDataBo;
import net.pladema.clinichistory.generalstate.service.domain.HCEInmunizationBo;
import net.pladema.clinichistory.generalstate.service.domain.HCEPersonalHistoryBo;
import net.pladema.clinichistory.generalstate.service.domain.Last2HCEVitalSignsBo;
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

    private final HCEInmunizationMapper hceInmunizationMapper;

    public HCEGeneralStateController(HCEGeneralStateService hceGeneralStateService,
                                     HCEClinicalObsGeneralStateService hceClinicalObsGeneralStateService,
                                     HCEGeneralStateMapper hceGeneralStateMapper, HCEInmunizationService hceInmunizationService, HCEInmunizationMapper hceInmunizationMapper) {
        this.hceGeneralStateService = hceGeneralStateService;
        this.hceClinicalObsGeneralStateService = hceClinicalObsGeneralStateService;
        this.hceGeneralStateMapper = hceGeneralStateMapper;
        this.hceInmunizationService = hceInmunizationService;
        this.hceInmunizationMapper = hceInmunizationMapper;
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
    public ResponseEntity<List<HCEInmunizationDto>> getInmunizationHistory(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug(LOGGING_INPUT, institutionId, patientId);
        List<HCEInmunizationBo> resultService = hceInmunizationService.getInmunizationHistory(patientId);
        List<HCEInmunizationDto> result = hceInmunizationMapper.toListHCEInmunizationDto(resultService);
        LOG.debug(LOGGING_OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}