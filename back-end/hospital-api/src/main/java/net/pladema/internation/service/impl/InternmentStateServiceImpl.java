package net.pladema.internation.service.impl;

import net.pladema.internation.service.InternmentStateService;
import net.pladema.internation.service.documents.anamnesis.AllergyService;
import net.pladema.internation.service.documents.anamnesis.HealthConditionService;
import net.pladema.internation.service.documents.anamnesis.VitalSignLabService;
import net.pladema.internation.service.documents.anamnesis.InmunizationService;
import net.pladema.internation.service.domain.InternmentGeneralState;
import net.pladema.internation.service.domain.ips.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Service
public class InternmentStateServiceImpl implements InternmentStateService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentStateServiceImpl.class);

    private static final String OUTPUT = "Output -> {}";

    private final AllergyService allergyService;

    private final InmunizationService inmunizationService;

    private final VitalSignLabService vitalSignLabService;

    private final HealthConditionService healthConditionService;

    public InternmentStateServiceImpl(AllergyService allergyService,
                                      InmunizationService inmunizationService,
                                      VitalSignLabService vitalSignLabService,
                                      HealthConditionService healthConditionService) {
        this.allergyService = allergyService;
        this.inmunizationService = inmunizationService;
        this.healthConditionService = healthConditionService;
        this.vitalSignLabService = vitalSignLabService;
    }

    @Override
    public InternmentGeneralState getInternmentGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        InternmentGeneralState internmentGeneralState = new InternmentGeneralState();
        loadGeneralHealthCondition(internmentEpisodeId, internmentGeneralState);
        internmentGeneralState.setVitalSigns(getVitalSignsState(internmentEpisodeId));
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

    private List<Medication> getMedicationsState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return Collections.emptyList();
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
        return vitalSignLabService.getAnthropometricDataGeneralState(internmentEpisodeId);
    }

    private List<VitalSignBo> getVitalSignsState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return vitalSignLabService.getLast2VitalSignsGeneralState(internmentEpisodeId);
    }
}
