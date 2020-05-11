package net.pladema.internation.service.internment.impl;

import net.pladema.internation.service.internment.InternmentStateService;
import net.pladema.internation.service.internment.domain.InternmentGeneralState;
import net.pladema.internation.service.ips.*;
import net.pladema.internation.service.ips.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class InternmentStateServiceImpl implements InternmentStateService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentStateServiceImpl.class);

    private static final String OUTPUT = "Output -> {}";
    
    private static final String LOGGING_INTERNMENT_EPISODE_ID = "Input parameters -> internmentEpisodeId {}";

    private final AllergyService allergyService;

    private final InmunizationService inmunizationService;

    private final MedicationService medicationService;

    private final ClinicalObservationService clinicalObservationService;

    private final HealthConditionService healthConditionService;

    public InternmentStateServiceImpl(AllergyService allergyService,
                                      InmunizationService inmunizationService,
                                      MedicationService medicationService,
                                      ClinicalObservationService clinicalObservationService,
                                      HealthConditionService healthConditionService) {
        this.allergyService = allergyService;
        this.inmunizationService = inmunizationService;
        this.medicationService = medicationService;
        this.healthConditionService = healthConditionService;
        this.clinicalObservationService = clinicalObservationService;
    }

    @Override
    public InternmentGeneralState getInternmentGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        InternmentGeneralState internmentGeneralState = new InternmentGeneralState();
        loadGeneralHealthCondition(internmentEpisodeId, internmentGeneralState);
        internmentGeneralState.setMedications(getMedicationsState(internmentEpisodeId));
        internmentGeneralState.setAllergies(getAllergiesState(internmentEpisodeId));
        internmentGeneralState.setVitalSigns(getVitalSignsState(internmentEpisodeId));
        internmentGeneralState.setAnthropometricData(getAntropometricDataState(internmentEpisodeId));
        internmentGeneralState.setInmunizations(getInmunizationsState(internmentEpisodeId));
        LOG.debug(OUTPUT, internmentGeneralState);
        return internmentGeneralState;
    }

    private void loadGeneralHealthCondition(Integer internmentEpisodeId, @NotNull InternmentGeneralState internmentGeneralState){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        GeneralHealthConditionBo generalHealthCondition = healthConditionService.getGeneralState(internmentEpisodeId);
        internmentGeneralState.setDiagnosis(generalHealthCondition.getDiagnosis());
        internmentGeneralState.setPersonalHistories(generalHealthCondition.getPersonalHistories());
        internmentGeneralState.setFamilyHistories(generalHealthCondition.getFamilyHistories());
    }

    private List<MedicationBo> getMedicationsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return medicationService.getMedicationsGeneralState(internmentEpisodeId);
    }

    private List<InmunizationBo> getInmunizationsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return inmunizationService.getInmunizationsGeneralState(internmentEpisodeId);
    }

    private List<AllergyConditionBo> getAllergiesState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return allergyService.getAllergiesGeneralState(internmentEpisodeId);
    }

    private AnthropometricDataBo getAntropometricDataState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return clinicalObservationService.getLastAnthropometricDataGeneralState(internmentEpisodeId);
    }

    private List<VitalSignBo> getVitalSignsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return clinicalObservationService.getLast2VitalSignsGeneralState(internmentEpisodeId);
    }
}
