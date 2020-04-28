package net.pladema.internation.controller;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.dto.core.InternmentGeneralStateDto;
import net.pladema.internation.controller.dto.ips.HealthConditionDto;
import net.pladema.internation.controller.dto.ips.HealthHistoryConditionDto;
import net.pladema.internation.controller.dto.ips.MedicationDto;
import net.pladema.internation.service.InternmentStateService;
import net.pladema.internation.service.documents.anamnesis.HealthConditionService;
import net.pladema.internation.service.documents.anamnesis.MedicationService;
import net.pladema.internation.service.domain.InternmentGeneralState;
import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryCondition;
import net.pladema.internation.service.domain.ips.Medication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/internments-state")
@Api(value = "Internment State", tags = { "Internment State" })
public class InternmentStateController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentStateController.class);

    private final InternmentStateService internmentStateService;

    private final HealthConditionService healthConditionService;

    private final MedicationService medicationService;

    public InternmentStateController(InternmentStateService internmentEpisodeService,
                                     HealthConditionService healthConditionService,
                                     MedicationService medicationService) {
        this.internmentStateService = internmentEpisodeService;
        this.healthConditionService = healthConditionService;
        this.medicationService = medicationService;
    }

    @GetMapping("/{internmentEpisodeId}/general")
    public ResponseEntity<InternmentGeneralStateDto> internmentGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        InternmentGeneralState interment = internmentStateService.getInternmentGeneralState(internmentEpisodeId);
        InternmentGeneralStateDto result = new InternmentGeneralStateDto();
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{internmentEpisodeId}/general/diagnosis")
    public ResponseEntity<List<HealthConditionDto>> diagnosisGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug("Imput parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<HealthConditionBo> diagnosis = healthConditionService.getDiagnosisGeneralState(internmentEpisodeId);
        List<HealthConditionDto> result = new ArrayList<>();
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{internmentEpisodeId}/general/personalHistories")
    public ResponseEntity<List<HealthHistoryConditionDto>> personalHistoriesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug("Imput parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<HealthHistoryCondition> personalHistories = healthConditionService.getPersonalHistoriesGeneralState(internmentEpisodeId);
        List<HealthHistoryConditionDto> result = new ArrayList<>();
                LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{internmentEpisodeId}/general/familyHistories")
    public ResponseEntity<List<HealthHistoryConditionDto>> familyHistoriesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug("Imput parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<HealthHistoryCondition> familyHistories = healthConditionService.getFamilyHistoriesGeneralState(internmentEpisodeId);
        List<HealthHistoryConditionDto> result = new ArrayList<>();
                LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{internmentEpisodeId}/general/medications")
    public ResponseEntity<List<MedicationDto>> medicationsGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug("Imput parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<Medication> medications = medicationService.getMedicationsGeneralState(internmentEpisodeId);
        List<MedicationDto> result = new ArrayList<>();
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

}