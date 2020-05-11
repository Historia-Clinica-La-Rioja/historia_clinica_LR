package net.pladema.staff.controller.service;

import net.pladema.internation.controller.internment.dto.HealthCareProfessionalGroupDto;
import net.pladema.internation.service.internment.impl.InternmentEpisodeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HealthcareProfessionalExternalServiceImpl implements HealthcareProfessionalExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeServiceImpl.class);

    private final HealthcareProfessionalService healthcareProfessionalService;

    public HealthcareProfessionalExternalServiceImpl(HealthcareProfessionalService healthcareProfessionalService){
        this.healthcareProfessionalService = healthcareProfessionalService;
    }

    @Override
    public HealthCareProfessionalGroupDto addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId) {
        LOG.debug("Input parameters -> internmentEpisode {}, healthcareProfessionalId {}", internmentEpisodeId, healthcareProfessionalId);
        HealthCareProfessionalGroupDto result = healthcareProfessionalService.addHealthcareProfessionalGroup(internmentEpisodeId, healthcareProfessionalId);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
