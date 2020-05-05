package net.pladema.internation.service.impl;

import net.pladema.internation.service.InternmentStateService;
import net.pladema.internation.service.documents.anamnesis.*;
import net.pladema.internation.service.domain.InternmentGeneralState;
import net.pladema.internation.service.domain.ips.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class InternmentStateServiceImpl implements InternmentStateService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentStateServiceImpl.class);

    private static final String OUTPUT = "Output -> {}";

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
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        InternmentGeneralState internmentGeneralState = new InternmentGeneralState();
        loadGeneralHealthCondition(internmentEpisodeId, internmentGeneralState);
        internmentGeneralState.setMedications(getMedicationsState(internmentEpisodeId));
        internmentGeneralState.setVitalSigns(getVitalSignsState(internmentEpisodeId));
        internmentGeneralState.setAnthropometricData(getAntropometricDataState(internmentEpisodeId));
        LOG.debug(OUTPUT, internmentGeneralState);
        return internmentGeneralState;
    }

    private void loadGeneralHealthCondition(Integer internmentEpisodeId, @NotNull InternmentGeneralState internmentGeneralState){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        GeneralHealthConditionBo generalHealthCondition = healthConditionService.getGeneralState(internmentEpisodeId);
        internmentGeneralState.setDiagnosis(generalHealthCondition.getDiagnosis());
        internmentGeneralState.setPersonalHistories(generalHealthCondition.getPersonalHistories());
        internmentGeneralState.setFamilyHistories(generalHealthCondition.getFamilyHistories());
    }

    private List<MedicationBo> getMedicationsState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return medicationService.getMedicationsGeneralState(internmentEpisodeId);
    }

    private List<InmunizationBo> getInmunizationsState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return inmunizationService.getInmunizationsGeneralState(internmentEpisodeId);
    }

    private List<AllergyConditionBo> getAllergiesState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return allergyService.getAllergiesGeneralState(internmentEpisodeId);
    }

    private List<AnthropometricDataBo> getAntropometricDataState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return clinicalObservationService.getLast2AnthropometricDataGeneralState(internmentEpisodeId);
    }

    private List<VitalSignBo> getVitalSignsState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return clinicalObservationService.getLast2VitalSignsGeneralState(internmentEpisodeId);
    }
}
