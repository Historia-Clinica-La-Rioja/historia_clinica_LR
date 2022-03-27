package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.domain.ips.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class FetchHospitalizationGeneralState {

    private static final Logger LOG = LoggerFactory.getLogger(FetchHospitalizationGeneralState.class);

    private static final String OUTPUT = "Output -> {}";
    
    private static final String LOGGING_INTERNMENT_EPISODE_ID = "Input parameters -> internmentEpisodeId {}";

    private final FetchHospitalizationAllergyState fetchHospitalizationAllergyState;

    private final FetchHospitalizationImmunizationState fetchHospitalizationImmunizationState;

    private final FetchHospitalizationMedicationState fetchHospitalizationMedicationState;

    private final FetchHospitalizationClinicalObservationState fetchHospitalizationClinicalObservationState;

    private final FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState;

    public FetchHospitalizationGeneralState(FetchHospitalizationAllergyState fetchHospitalizationAllergyState,
                                            FetchHospitalizationImmunizationState fetchHospitalizationImmunizationState,
                                            FetchHospitalizationMedicationState fetchHospitalizationMedicationState,
                                            FetchHospitalizationClinicalObservationState fetchHospitalizationClinicalObservationState,
                                            FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState) {
        this.fetchHospitalizationAllergyState = fetchHospitalizationAllergyState;
        this.fetchHospitalizationImmunizationState = fetchHospitalizationImmunizationState;
        this.fetchHospitalizationMedicationState = fetchHospitalizationMedicationState;
        this.fetchHospitalizationHealthConditionState = fetchHospitalizationHealthConditionState;
        this.fetchHospitalizationClinicalObservationState = fetchHospitalizationClinicalObservationState;
    }

    public HospitalizationGeneralState getInternmentGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        HospitalizationGeneralState hospitalizationGeneralState = new HospitalizationGeneralState();
        loadGeneralHealthCondition(internmentEpisodeId, hospitalizationGeneralState);
        hospitalizationGeneralState.setMedications(getMedicationsState(internmentEpisodeId));
        hospitalizationGeneralState.setAllergies(getAllergiesState(internmentEpisodeId));
        hospitalizationGeneralState.setRiskFactors(getRiskFactorsState(internmentEpisodeId));
        hospitalizationGeneralState.setAnthropometricData(getAnthropometricDataState(internmentEpisodeId));
        hospitalizationGeneralState.setImmunizations(getImmunizationsState(internmentEpisodeId));
        LOG.debug(OUTPUT, hospitalizationGeneralState);
        return hospitalizationGeneralState;
    }

    private void loadGeneralHealthCondition(Integer internmentEpisodeId, @NotNull HospitalizationGeneralState hospitalizationGeneralState){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        GeneralHealthConditionBo generalHealthCondition = fetchHospitalizationHealthConditionState.getGeneralState(internmentEpisodeId);
        hospitalizationGeneralState.setMainDiagnosis(generalHealthCondition.getMainDiagnosis());
        hospitalizationGeneralState.setDiagnosis(generalHealthCondition.getDiagnosis());
        hospitalizationGeneralState.setPersonalHistories(generalHealthCondition.getPersonalHistories());
        hospitalizationGeneralState.setFamilyHistories(generalHealthCondition.getFamilyHistories());
    }

    private List<MedicationBo> getMedicationsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return fetchHospitalizationMedicationState.run(internmentEpisodeId);
    }

    private List<ImmunizationBo> getImmunizationsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return fetchHospitalizationImmunizationState.run(internmentEpisodeId);
    }

    private List<AllergyConditionBo> getAllergiesState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return fetchHospitalizationAllergyState.run(internmentEpisodeId);
    }

    private AnthropometricDataBo getAnthropometricDataState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return fetchHospitalizationClinicalObservationState.getLastAnthropometricDataGeneralState(internmentEpisodeId);
    }

    private Last2RiskFactorsBo getRiskFactorsState(Integer internmentEpisodeId){
        LOG.debug(LOGGING_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        return fetchHospitalizationClinicalObservationState.getLast2RiskFactorsGeneralState(internmentEpisodeId);
    }
}
