package net.pladema.clinichistory.external.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.external.controller.dto.ExternalClinicalHistoryDto;
import net.pladema.clinichistory.external.controller.mapper.ExternalClinicalHistoryMapper;
import net.pladema.clinichistory.external.service.ExternalClinicalHistoryService;
import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistoryBo;
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
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/outpatient/consultations")
@Validated
@Tag(name = "External Clinical History", description = "External Clinical History")
public class ExternalClinicalHistoryController {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalClinicalHistoryController.class);
    public static final String OUTPUT = "Output -> {}";
    private final ExternalClinicalHistoryService externalClinicalHistoryService;
    private final ExternalClinicalHistoryMapper externalClinicalHistoryMapper;

    public ExternalClinicalHistoryController(ExternalClinicalHistoryService externalClinicalHistoryService, ExternalClinicalHistoryMapper externalClinicalHistoryMapper) {
        this.externalClinicalHistoryService = externalClinicalHistoryService;
        this.externalClinicalHistoryMapper = externalClinicalHistoryMapper;
    }

    @GetMapping("/getExternalClinicalHistoryList")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<List<ExternalClinicalHistoryDto>> getExternalClinicalHistoryList(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {
        List<ExternalClinicalHistoryBo> evolutions = externalClinicalHistoryService.getExternalClinicalHistory(patientId);
        List<ExternalClinicalHistoryDto> result = externalClinicalHistoryMapper.fromListExternalClinicalHistoryBo(evolutions);
        LOG.debug("Get list => {}", result);
        return ResponseEntity.ok(result);
    }
}
