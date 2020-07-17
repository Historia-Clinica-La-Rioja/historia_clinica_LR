package net.pladema.staff.controller.service;

import net.pladema.clinichistory.hospitalization.controller.dto.HealthCareProfessionalGroupDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.HealthCareProfessionalGroupMapper;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.staff.service.HealthcareProfessionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HealthcareProfessionalExternalServiceImpl implements HealthcareProfessionalExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalExternalServiceImpl.class);

    private final HealthcareProfessionalService healthcareProfessionalService;

    private final HealthCareProfessionalGroupMapper healthCareProfessionalGroupMapper;

    public HealthcareProfessionalExternalServiceImpl(HealthcareProfessionalService healthcareProfessionalService,
                                                     HealthCareProfessionalGroupMapper healthCareProfessionalGroupMapper){
        this.healthcareProfessionalService = healthcareProfessionalService;
        this.healthCareProfessionalGroupMapper = healthCareProfessionalGroupMapper;
    }

    @Override
    public HealthCareProfessionalGroupDto addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId) {
        LOG.debug("Input parameters -> internmentEpisode {}, healthcareProfessionalId {}", internmentEpisodeId, healthcareProfessionalId);
        HealthcareProfessionalGroup resultService = healthcareProfessionalService.addHealthcareProfessionalGroup(internmentEpisodeId, healthcareProfessionalId);
        HealthCareProfessionalGroupDto result = healthCareProfessionalGroupMapper.toHealthcareProfessionalGroupDto(resultService);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Integer getProfessionalId(Integer userId) {
        LOG.debug("Input parameters -> userId {}", userId);
        Integer result = healthcareProfessionalService.getProfessionalId(userId);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
