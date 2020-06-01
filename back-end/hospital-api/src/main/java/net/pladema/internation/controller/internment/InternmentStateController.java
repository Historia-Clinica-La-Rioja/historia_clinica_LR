package net.pladema.internation.controller.internment;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.internment.dto.InternmentGeneralStateDto;
import net.pladema.internation.controller.internment.dto.Last2VitalSignsDto;
import net.pladema.internation.controller.ips.dto.*;
import net.pladema.internation.controller.internment.mapper.InternmentStateMapper;
import net.pladema.internation.service.internment.InternmentStateService;
import net.pladema.internation.service.internment.domain.InternmentGeneralState;
import net.pladema.internation.service.internment.domain.Last2VitalSignsBo;
import net.pladema.internation.service.ips.*;
import net.pladema.internation.service.ips.domain.*;
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
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO') || "
		+ "hasPermission(#institutionId, 'PROFESIONAL_DE_SALUD')")
public class InternmentStateController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentStateController.class);
    
    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE = "Input parameters -> institutionId {}, internmentEpisodeId {}";

    private final InternmentStateService internmentStateService;

    private final HealthConditionService healthConditionService;

    private final MedicationService medicationService;
    
    private final AllergyService allergyService;

    private final InmunizationService inmunizationService;

    private final ClinicalObservationService clinicalObservationService;

    private final InternmentStateMapper internmentStateMapper;

    public InternmentStateController(InternmentStateService internmentStateService,
                                     HealthConditionService healthConditionService,
                                     MedicationService medicationService,
                                     AllergyService allergyService,
                                     InmunizationService inmunizationService,
                                     InternmentStateMapper internmentStateMapper,
                                     ClinicalObservationService clinicalObservationService) {
        this.internmentStateService = internmentStateService;
        this.healthConditionService = healthConditionService;
        this.medicationService = medicationService;
        this.allergyService = allergyService;
        this.inmunizationService = inmunizationService;
        this.internmentStateMapper = internmentStateMapper;
        this.clinicalObservationService = clinicalObservationService;
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general")
    public ResponseEntity<InternmentGeneralStateDto> internmentGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        InternmentGeneralState interment = internmentStateService.getInternmentGeneralState(internmentEpisodeId);
        InternmentGeneralStateDto result = internmentStateMapper.toInternmentGeneralStateDto(interment);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/maindiagnosis")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO') || "
    		+ "hasPermission(#institutionId, 'PROFESIONAL_DE_SALUD')")
    public ResponseEntity<HealthConditionDto> mainDiagnosisGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug("Imput parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        HealthConditionBo mainDiagnosis = healthConditionService.getMainDiagnosisGeneralState(internmentEpisodeId);
        HealthConditionDto result = internmentStateMapper.toHealthConditionDto(mainDiagnosis);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/diagnosis")
    public ResponseEntity<List<DiagnosisDto>> diagnosisGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<DiagnosisBo> diagnosis = healthConditionService.getDiagnosisGeneralState(internmentEpisodeId);
        List<DiagnosisDto> result = internmentStateMapper.toListDiagnosisDto(diagnosis);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/personalHistories")
    public ResponseEntity<List<HealthHistoryConditionDto>> personalHistoriesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<HealthHistoryConditionBo> personalHistories = healthConditionService.getPersonalHistoriesGeneralState(internmentEpisodeId);
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
        List<HealthHistoryConditionBo> familyHistories = healthConditionService.getFamilyHistoriesGeneralState(internmentEpisodeId);
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
        List<MedicationBo> medicationBos = medicationService.getMedicationsGeneralState(internmentEpisodeId);
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
        AnthropometricDataBo anthropometricDatas = clinicalObservationService.getLastAnthropometricDataGeneralState(internmentEpisodeId);
        AnthropometricDataDto result = internmentStateMapper.toAnthropometricDataDto(anthropometricDatas);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/vitalSigns")
    public ResponseEntity<Last2VitalSignsDto> vitalSignsGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        Last2VitalSignsBo vitalSignBos = clinicalObservationService.getLast2VitalSignsGeneralState(internmentEpisodeId);
        Last2VitalSignsDto result = internmentStateMapper.toLast2VitalSignDto(vitalSignBos);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/inmunizations")
    public ResponseEntity<List<InmunizationDto>> inmunizationsGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<InmunizationBo> inmunizationBos = inmunizationService.getInmunizationsGeneralState(internmentEpisodeId);
        List<InmunizationDto> result = internmentStateMapper.toListInmunizationDto(inmunizationBos);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/general/allergies")
    public ResponseEntity<List<AllergyConditionDto>> allergiesGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug(LOGGING_INSTITUTION_AND_INTERNMENT_EPISODE, institutionId, internmentEpisodeId);
        List<AllergyConditionBo> allergyConditionBos = allergyService.getAllergiesGeneralState(internmentEpisodeId);
        List<AllergyConditionDto> result = internmentStateMapper.toListAllergyConditionDto(allergyConditionBos);
        LOG.debug(LOGGING_OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

}