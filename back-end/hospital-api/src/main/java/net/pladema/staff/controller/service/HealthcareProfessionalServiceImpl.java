package net.pladema.staff.controller.service;

import net.pladema.clinichistory.hospitalization.controller.dto.HealthCareProfessionalGroupDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.HealthCareProfessionalGroupMapper;
import net.pladema.clinichistory.hospitalization.repository.HealthcareProfessionalGroupRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HealthcareProfessionalServiceImpl implements  HealthcareProfessionalService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalServiceImpl.class);

    private final HealthcareProfessionalGroupRepository healthcareProfessionalGroupRepository;

    private final HealthCareProfessionalGroupMapper healthCareProfessionalGroupMapper;

    public HealthcareProfessionalServiceImpl(HealthcareProfessionalGroupRepository healthcareProfessionalGroupRepository, HealthCareProfessionalGroupMapper healthCareProfessionalGroupMapper) {
        this.healthcareProfessionalGroupRepository = healthcareProfessionalGroupRepository;
        this.healthCareProfessionalGroupMapper = healthCareProfessionalGroupMapper;
    }

    @Override
    public HealthCareProfessionalGroupDto addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId) {
        LOG.debug("Input parameters -> internmentEpisode {}, healthcareProfessionalId {}", internmentEpisodeId, healthcareProfessionalId);
        HealthcareProfessionalGroup healthcareProfessionalGroupToSave = new HealthcareProfessionalGroup(internmentEpisodeId, healthcareProfessionalId);
        healthcareProfessionalGroupToSave.setResponsible(true);
        HealthcareProfessionalGroup saved = healthcareProfessionalGroupRepository.save(healthcareProfessionalGroupToSave);
        HealthCareProfessionalGroupDto result = healthCareProfessionalGroupMapper.toHealthcareProfessionalGroupDto(saved);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
