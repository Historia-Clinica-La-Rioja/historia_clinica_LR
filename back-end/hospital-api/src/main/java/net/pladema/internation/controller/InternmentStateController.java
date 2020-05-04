package net.pladema.internation.controller;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.dto.core.InternmentGeneralStateDto;
import net.pladema.internation.controller.dto.ips.*;
import net.pladema.internation.controller.mapper.InternmentStateMapper;
import net.pladema.internation.service.InternmentStateService;
import net.pladema.internation.service.documents.anamnesis.*;
import net.pladema.internation.service.domain.InternmentGeneralState;
import net.pladema.internation.service.domain.ips.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/internments-state")
@Api(value = "Internment State", tags = { "Internment State" })
@Validated
public class InternmentStateController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentStateController.class);

    private final InternmentStateService internmentStateService;

    private final HealthConditionService healthConditionService;

    private final MedicationService medicationService;
    
    private final AllergyService allergyService;

    private final InmunizationService inmunizationService;

    private final VitalSignLabService vitalSignLabService;

    private final InternmentStateMapper internmentStateMapper;

    public InternmentStateController(InternmentStateService internmentStateService,
                                     HealthConditionService healthConditionService,
                                     MedicationService medicationService,
                                     AllergyService allergyService,
                                     InmunizationService inmunizationService,
                                     InternmentStateMapper internmentStateMapper,
                                     VitalSignLabService vitalSignLabService) {
        this.internmentStateService = internmentStateService;
        this.healthConditionService = healthConditionService;
        this.medicationService = medicationService;
        this.allergyService = allergyService;
        this.inmunizationService = inmunizationService;
        this.internmentStateMapper = internmentStateMapper;
        this.vitalSignLabService = vitalSignLabService;
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general")
    public ResponseEntity<InternmentGeneralStateDto> internmentGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        InternmentGeneralState interment = internmentStateService.getInternmentGeneralState(internmentEpisodeId);
        InternmentGeneralStateDto result = internmentStateMapper.toInternmentGeneralStateDto(interment);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/diagnosis")
    public ResponseEntity<List<DiagnosisDto>> diagnosisGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug("Imput parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<DiagnosisBo> diagnosis = healthConditionService.getDiagnosisGeneralState(internmentEpisodeId);
        List<DiagnosisDto> result = internmentStateMapper.toListDiagnosisDto(diagnosis);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/personalHistories")
    public ResponseEntity<List<HealthHistoryConditionDto>> personalHistoriesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug("Imput parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<HealthHistoryConditionBo> personalHistories = healthConditionService.getPersonalHistoriesGeneralState(internmentEpisodeId);
        List<HealthHistoryConditionDto> result = internmentStateMapper.toListHealthHistoryConditionDto(personalHistories);
                LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/familyHistories")
    public ResponseEntity<List<HealthHistoryConditionDto>> familyHistoriesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug("Imput parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<HealthHistoryConditionBo> familyHistories = healthConditionService.getFamilyHistoriesGeneralState(internmentEpisodeId);
        List<HealthHistoryConditionDto> result = internmentStateMapper.toListHealthHistoryConditionDto(familyHistories);
                LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/medications")
    public ResponseEntity<List<MedicationDto>> medicationsGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug("Imput parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<MedicationBo> medicationBos = medicationService.getMedicationsGeneralState(internmentEpisodeId);
        List<MedicationDto> result = internmentStateMapper.toListMedicationDto(medicationBos);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/anthropometricData")
    public ResponseEntity<List<AnthropometricDataDto>> anthropometricDataGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<AnthropometricDataBo> anthropometricDatas = vitalSignLabService.getAnthropometricDataGeneralState(internmentEpisodeId);
        List<AnthropometricDataDto> result = new ArrayList<>();
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/vitalSigns")
    public ResponseEntity<List<VitalSignDto>> vitalSignsGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<VitalSignBo> vitalSignBos = vitalSignLabService.getLast2VitalSignsGeneralState(internmentEpisodeId);
        List<VitalSignDto> result = internmentStateMapper.toListVitalSignDto(vitalSignBos);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/inmunizations")
    public ResponseEntity<List<InmunizationDto>> inmunizationsGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<InmunizationBo> inmunizationBos = inmunizationService.getInmunizationsGeneralState(internmentEpisodeId);
        List<InmunizationDto> result = new ArrayList<>();
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/allergies")
    public ResponseEntity<List<AllergyConditionDto>> allergiesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        List<AllergyConditionBo> allergyConditionBos = allergyService.getAllergiesGeneralState(internmentEpisodeId);
        List<AllergyConditionDto> result = new ArrayList<>();
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

}