package net.pladema.clinichistory.documents.core.generalstate;

import ar.lamansys.sgh.clinichistory.domain.ips.*;
import net.pladema.clinichistory.documents.service.generalstate.*;
import net.pladema.clinichistory.hospitalization.service.domain.Last2VitalSignsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class EncounterGeneralStateBuilderImpl implements EncounterGeneralStateBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(EncounterGeneralStateBuilderImpl.class);

    private static final String OUTPUT = "Output -> {}";
    
    private static final String LOGGING_INTERNMENT_EPISODE_ID = "Input parameters -> internmentEpisodeId {}";

    private final AllergyGeneralStateService allergyGeneralStateService;

    private final ImmunizationGeneralStateService immunizationGeneralStateService;

    private final MedicationGeneralStateService medicationGeneralStateService;

    private final ClinicalObservationGeneralStateService clinicalObservationGeneralStateService;

    private final HealthConditionGeneralStateService healthConditionGeneralStateService;

    public EncounterGeneralStateBuilderImpl(AllergyGeneralStateService allergyGeneralStateService,
                                            ImmunizationGeneralStateService immunizationGeneralStateService,
                                            MedicationGeneralStateService medicationGeneralStateService,
                                            ClinicalObservationGeneralStateService clinicalObservationGeneralStateService,
                                            HealthConditionGeneralStateService healthConditionGeneralStateService) {
        this.allergyGeneralStateService = allergyGeneralStateService;
        this.immunizationGeneralStateService = immunizationGeneralStateService;
        this.medicationGeneralStateService = medicationGeneralStateService;
        this.healthConditionGeneralStateService = healthConditionGeneralStateService;
        this.clinicalObservationGeneralStateService = clinicalObservationGeneralStateService;
    }

    @Override
    public EncounterGeneralState getInternmentGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        EncounterGeneralState encounterGeneralState = new EncounterGeneralState();
        loadGeneralHealthCondition(internmentEpisodeId, encounterGeneralState);
        encounterGeneralState.setMedications(getMedicationsState(internmentEpisodeId));
        encounterGeneralState.setAllergies(getAllergiesState(internmentEpisodeId));
        encounterGeneralState.setVitalSigns(getVitalSignsState(internmentEpisodeId));
        encounterGeneralState.setAnthropometricData(getAnthropometricDataState(internmentEpisodeId));
        encounterGeneralState.setImmunizations(getImmunizationsState(internmentEpisodeId));
        LOG.debug(OUTPUT, encounterGeneralState);
        return encounterGeneralState;
    }

    private void loadGeneralHealthCondition(Integer internmentEpisodeId, @NotNull EncounterGeneralState encounterGeneralState){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        GeneralHealthConditionBo generalHealthCondition = healthConditionGeneralStateService.getGeneralState(internmentEpisodeId);
        encounterGeneralState.setMainDiagnosis(generalHealthCondition.getMainDiagnosis());
        encounterGeneralState.setDiagnosis(generalHealthCondition.getDiagnosis());
        encounterGeneralState.setPersonalHistories(generalHealthCondition.getPersonalHistories());
        encounterGeneralState.setFamilyHistories(generalHealthCondition.getFamilyHistories());
    }

    private List<MedicationBo> getMedicationsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return medicationGeneralStateService.getMedicationsGeneralState(internmentEpisodeId);
    }

    private List<ImmunizationBo> getImmunizationsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return immunizationGeneralStateService.getImmunizationsGeneralState(internmentEpisodeId);
    }

    private List<AllergyConditionBo> getAllergiesState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return allergyGeneralStateService.getAllergiesGeneralState(internmentEpisodeId);
    }

    private AnthropometricDataBo getAnthropometricDataState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return clinicalObservationGeneralStateService.getLastAnthropometricDataGeneralState(internmentEpisodeId);
    }

    private Last2VitalSignsBo getVitalSignsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return clinicalObservationGeneralStateService.getLast2VitalSignsGeneralState(internmentEpisodeId);
    }
}
