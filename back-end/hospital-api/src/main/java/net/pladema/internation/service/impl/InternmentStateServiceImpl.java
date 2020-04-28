package net.pladema.internation.service.impl;

import net.pladema.internation.service.InternmentStateService;
import net.pladema.internation.service.documents.anamnesis.AllergyService;
import net.pladema.internation.service.documents.anamnesis.CreateVitalSignLabService;
import net.pladema.internation.service.documents.anamnesis.InmunizationService;
import net.pladema.internation.service.domain.InternmentGeneralState;
import net.pladema.internation.service.domain.ips.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class InternmentStateServiceImpl implements InternmentStateService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentStateServiceImpl.class);

    private final AllergyService allergyService;

    private final InmunizationService inmunizationService;

    private final CreateVitalSignLabService createVitalSignLabService;

    public InternmentStateServiceImpl(AllergyService allergyService, InmunizationService inmunizationService, CreateVitalSignLabService createVitalSignLabService) {
        this.allergyService = allergyService;
        this.inmunizationService = inmunizationService;
        this.createVitalSignLabService = createVitalSignLabService;
    }

    @Override
    public InternmentGeneralState getInternmentGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return new InternmentGeneralState();
    }

    private List<HealthConditionBo> getDiagnosisState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return Collections.emptyList();
    }

    private List<HealthHistoryCondition> getPersonalHistoriesState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return Collections.emptyList();
    }

    private List<HealthHistoryCondition> getFamilyHistoriesState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return Collections.emptyList();
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
        return createVitalSignLabService.getAnthropometricDataGeneralState(internmentEpisodeId);
    }

    private List<VitalSignBo> getVitalSignsState(Integer internmentEpisodeId){
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return createVitalSignLabService.getVitalSignsGeneralState(internmentEpisodeId);
    }
}
