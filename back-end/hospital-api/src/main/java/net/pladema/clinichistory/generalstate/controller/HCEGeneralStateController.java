package net.pladema.clinichistory.generalstate.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.generalstate.controller.dto.HCEPersonalHistoryDto;
import net.pladema.clinichistory.generalstate.controller.mapper.HCEGeneralStateMapper;
import net.pladema.clinichistory.generalstate.service.HCEGeneralStateService;
import net.pladema.clinichistory.generalstate.service.domain.HCEPersonalHistoryBo;
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

    private final HCEGeneralStateMapper hceGeneralStateMapper;

    public HCEGeneralStateController(HCEGeneralStateService hceGeneralStateService,
                                     HCEGeneralStateMapper hceGeneralStateMapper) {
        this.hceGeneralStateService = hceGeneralStateService;
        this.hceGeneralStateMapper = hceGeneralStateMapper;
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
}