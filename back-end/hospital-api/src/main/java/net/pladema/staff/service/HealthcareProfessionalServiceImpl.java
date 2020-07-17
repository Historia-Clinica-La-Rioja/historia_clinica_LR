package net.pladema.staff.service;

import net.pladema.clinichistory.hospitalization.repository.HealthcareProfessionalGroupRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HealthcareProfessionalServiceImpl implements  HealthcareProfessionalService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalServiceImpl.class);

    private final HealthcareProfessionalGroupRepository healthcareProfessionalGroupRepository;

    private final HealthcareProfessionalRepository healthcareProfessionalRepository;

    public HealthcareProfessionalServiceImpl(HealthcareProfessionalGroupRepository healthcareProfessionalGroupRepository,
                                             HealthcareProfessionalRepository healthcareProfessionalRepository) {
        this.healthcareProfessionalGroupRepository = healthcareProfessionalGroupRepository;
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
    }

    @Override
    public HealthcareProfessionalGroup addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId) {
        LOG.debug("Input parameters -> internmentEpisode {}, healthcareProfessionalId {}", internmentEpisodeId, healthcareProfessionalId);
        HealthcareProfessionalGroup healthcareProfessionalGroupToSave = new HealthcareProfessionalGroup(internmentEpisodeId, healthcareProfessionalId);
        healthcareProfessionalGroupToSave.setResponsible(true);
        HealthcareProfessionalGroup result = healthcareProfessionalGroupRepository.save(healthcareProfessionalGroupToSave);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Integer getProfessionalId(Integer userId) {
        LOG.debug("Input parameters -> userId {}", userId);
        Integer result = healthcareProfessionalRepository.getProfessionalId(userId);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
