package net.pladema.clinichistory.hospitalization.service.impl;

import net.pladema.clinichistory.hospitalization.service.InternmentStateService;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentGeneralState;
import net.pladema.clinichistory.hospitalization.service.domain.Last2VitalSignsBo;
import net.pladema.clinichistory.hospitalization.service.generalstate.*;
import net.pladema.clinichistory.ips.service.domain.*;
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

    private final AllergyGeneralStateService allergyGeneralStateService;

    private final InmunizationGeneralStateService inmunizationGeneralStateService;

    private final MedicationGeneralStateService medicationGeneralStateService;

    private final ClinicalObservationGeneralStateService clinicalObservationGeneralStateService;

    private final HealthConditionGeneralStateService healthConditionGeneralStateService;

    public InternmentStateServiceImpl(AllergyGeneralStateService allergyGeneralStateService,
                                      InmunizationGeneralStateService inmunizationGeneralStateService,
                                      MedicationGeneralStateService medicationGeneralStateService,
                                      ClinicalObservationGeneralStateService clinicalObservationGeneralStateService,
                                      HealthConditionGeneralStateService healthConditionGeneralStateService) {
        this.allergyGeneralStateService = allergyGeneralStateService;
        this.inmunizationGeneralStateService = inmunizationGeneralStateService;
        this.medicationGeneralStateService = medicationGeneralStateService;
        this.healthConditionGeneralStateService = healthConditionGeneralStateService;
        this.clinicalObservationGeneralStateService = clinicalObservationGeneralStateService;
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
        GeneralHealthConditionBo generalHealthCondition = healthConditionGeneralStateService.getGeneralState(internmentEpisodeId);
        internmentGeneralState.setMainDiagnosis(generalHealthCondition.getMainDiagnosis());
        internmentGeneralState.setDiagnosis(generalHealthCondition.getDiagnosis());
        internmentGeneralState.setPersonalHistories(generalHealthCondition.getPersonalHistories());
        internmentGeneralState.setFamilyHistories(generalHealthCondition.getFamilyHistories());
    }

    private List<MedicationBo> getMedicationsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return medicationGeneralStateService.getMedicationsGeneralState(internmentEpisodeId);
    }

    private List<InmunizationBo> getInmunizationsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return inmunizationGeneralStateService.getInmunizationsGeneralState(internmentEpisodeId);
    }

    private List<AllergyConditionBo> getAllergiesState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return allergyGeneralStateService.getAllergiesGeneralState(internmentEpisodeId);
    }

    private AnthropometricDataBo getAntropometricDataState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return clinicalObservationGeneralStateService.getLastAnthropometricDataGeneralState(internmentEpisodeId);
    }

    private Last2VitalSignsBo getVitalSignsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return clinicalObservationGeneralStateService.getLast2VitalSignsGeneralState(internmentEpisodeId);
    }
}
