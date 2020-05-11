package net.pladema.staff.controller.service;

import net.pladema.internation.controller.internment.dto.HealthCareProfessionalGroupDto;
import net.pladema.internation.controller.internment.mapper.HealthCareProfessionalGroupMapper;
import net.pladema.internation.repository.core.HealthcareProfessionalGroupRepository;
import net.pladema.internation.repository.core.entity.HealthcareProfessionalGroup;
import net.pladema.internation.service.internment.impl.InternmentEpisodeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HealthcareProfessionalServiceImpl implements  HealthcareProfessionalService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeServiceImpl.class);

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
