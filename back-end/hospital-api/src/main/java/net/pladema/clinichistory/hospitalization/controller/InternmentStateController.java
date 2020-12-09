package net.pladema.clinichistory.hospitalization.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.service.generalstate.*;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentGeneralStateDto;
import net.pladema.clinichistory.hospitalization.controller.dto.internmentstate.DiagnosesGeneralStateDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.*;
import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentStateMapper;
import net.pladema.clinichistory.hospitalization.service.domain.Last2VitalSignsBo;
import net.pladema.clinichistory.documents.service.ips.domain.*;
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
@RequestMapping("/institutions/{institutionId}/internments-state")
@Api(value = "Internment State", tags = { "Internment State" })
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD')")
public class InternmentStateController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentStateController.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE = "Input parameters -> institutionId {}, internmentEpisodeId {}";

    private final EncounterGeneralStateBuilder encounterGeneralStateBuilder;

    private final HealthConditionGeneralStateService healthConditionGeneralStateService;

    private final MedicationGeneralStateService medicationGeneralStateService;

    private final AllergyGeneralStateService allergyGeneralStateServiceService;

    private final ImmunizationGeneralStateService immunizationGeneralStateService;

    private final ClinicalObservationGeneralStateService clinicalObservationGeneralStateService;

    private final InternmentStateMapper internmentStateMapper;

    public InternmentStateController(EncounterGeneralStateBuilder encounterGeneralStateBuilder,
                                     HealthConditionGeneralStateService healthConditionGeneralStateService,
                                     MedicationGeneralStateService medicationGeneralStateService,
                                     AllergyGeneralStateService allergyGeneralStateServiceService,
                                     ImmunizationGeneralStateService immunizationGeneralStateService,
                                     InternmentStateMapper internmentStateMapper,
                                     ClinicalObservationGeneralStateService clinicalObservationGeneralStateService) {
        this.encounterGeneralStateBuilder = encounterGeneralStateBuilder;
        this.healthConditionGeneralStateService = healthConditionGeneralStateService;
        this.medicationGeneralStateService = medicationGeneralStateService;
        this.allergyGeneralStateServiceService = allergyGeneralStateServiceService;
        this.immunizationGeneralStateService = immunizationGeneralStateService;
        this.internmentStateMapper = internmentStateMapper;
        this.clinicalObservationGeneralStateService = clinicalObservationGeneralStateService;
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general")
    public ResponseEntity<InternmentGeneralStateDto> internmentGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        EncounterGeneralState interment = encounterGeneralStateBuilder.getInternmentGeneralState(internmentEpisodeId);
        InternmentGeneralStateDto result = internmentStateMapper.toInternmentGeneralStateDto(interment);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/maindiagnosis")
    public ResponseEntity<HealthConditionDto> mainDiagnosisGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        HealthConditionBo mainDiagnosis = healthConditionGeneralStateService.getMainDiagnosisGeneralState(internmentEpisodeId);
        HealthConditionDto result = internmentStateMapper.toHealthConditionDto(mainDiagnosis);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/alternativeDiagnoses")
    public ResponseEntity<List<DiagnosisDto>> getAlternativeDiagnosesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<DiagnosisBo> diagnosis = healthConditionGeneralStateService.getAlternativeDiagnosisGeneralState(internmentEpisodeId);
        List<DiagnosisDto> result = internmentStateMapper.toListDiagnosisDto(diagnosis);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/alternativeDiagnoses/active")
    public ResponseEntity<List<DiagnosisDto>> getActiveAlternativeDiagnosesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<DiagnosisBo> diagnosis = healthConditionGeneralStateService.getActiveAlternativeDiagnosesGeneralState(internmentEpisodeId);
        List<DiagnosisDto> result = internmentStateMapper.toListDiagnosisDto(diagnosis);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/diagnoses")
    public ResponseEntity<List<DiagnosesGeneralStateDto>> getDiagnosesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<HealthConditionBo> diagnoses = healthConditionGeneralStateService.getDiagnosesGeneralState(internmentEpisodeId);
        List<DiagnosesGeneralStateDto> result = internmentStateMapper.toListDiagnosesGeneralStateDto(diagnoses);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/personalHistories")
    public ResponseEntity<List<HealthHistoryConditionDto>> personalHistoriesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<HealthHistoryConditionBo> personalHistories = healthConditionGeneralStateService.getPersonalHistoriesGeneralState(internmentEpisodeId);
        List<HealthHistoryConditionDto> result = internmentStateMapper.toListHealthHistoryConditionDto(personalHistories);
                LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/familyHistories")
    public ResponseEntity<List<HealthHistoryConditionDto>> familyHistoriesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<HealthHistoryConditionBo> familyHistories = healthConditionGeneralStateService.getFamilyHistoriesGeneralState(internmentEpisodeId);
        List<HealthHistoryConditionDto> result = internmentStateMapper.toListHealthHistoryConditionDto(familyHistories);
                LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/medications")
    public ResponseEntity<List<MedicationDto>> medicationsGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<MedicationBo> medicationBos = medicationGeneralStateService.getMedicationsGeneralState(internmentEpisodeId);
        List<MedicationDto> result = internmentStateMapper.toListMedicationDto(medicationBos);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/anthropometricData")
    public ResponseEntity<AnthropometricDataDto> anthropometricDataGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        AnthropometricDataBo anthropometricData = clinicalObservationGeneralStateService.getLastAnthropometricDataGeneralState(internmentEpisodeId);
        AnthropometricDataDto result = internmentStateMapper.toAnthropometricDataDto(anthropometricData);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/vitalSigns")
    public ResponseEntity<Last2VitalSignsDto> vitalSignsGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        Last2VitalSignsBo vitalSignBos = clinicalObservationGeneralStateService.getLast2VitalSignsGeneralState(internmentEpisodeId);
        Last2VitalSignsDto result = internmentStateMapper.toLast2VitalSignDto(vitalSignBos);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/immunizations")
    public ResponseEntity<List<ImmunizationDto>> immunizationsGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<ImmunizationBo> immunizationBos = immunizationGeneralStateService.getImmunizationsGeneralState(internmentEpisodeId);
        List<ImmunizationDto> result = internmentStateMapper.toListImmunizationDto(immunizationBos);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/allergies")
    public ResponseEntity<List<AllergyConditionDto>> allergiesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<AllergyConditionBo> allergyConditionBos = allergyGeneralStateServiceService.getAllergiesGeneralState(internmentEpisodeId);
        List<AllergyConditionDto> result = internmentStateMapper.toListAllergyConditionDto(allergyConditionBos);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

}