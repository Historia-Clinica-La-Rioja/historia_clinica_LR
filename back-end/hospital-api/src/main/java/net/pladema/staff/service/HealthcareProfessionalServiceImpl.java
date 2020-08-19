package net.pladema.staff.service;

import net.pladema.clinichistory.hospitalization.repository.HealthcareProfessionalGroupRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealthcareProfessionalServiceImpl implements  HealthcareProfessionalService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";

    private final HealthcareProfessionalGroupRepository healthcareProfessionalGroupRepository;

    private final HealthcareProfessionalRepository healthcareProfessionalRepository;

    public HealthcareProfessionalServiceImpl(HealthcareProfessionalGroupRepository healthcareProfessionalGroupRepository,
                                             HealthcareProfessionalRepository healthcareProfessionalRepository) {
        this.healthcareProfessionalGroupRepository = healthcareProfessionalGroupRepository;
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
    }

    @Override
    public List<HealthcarePersonBo> getAllDoctorsByInstitution(Integer institutionId) {
        LOG.debug("Input parameters -> institutionId {}", institutionId);
        List<HealthcarePersonBo> result = healthcareProfessionalRepository.getAllDoctors(institutionId);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<HealthcareProfessionalBo> getAll(Integer institutionId) {
        LOG.debug("Input parameters -> institutionId {}", institutionId);
        List<HealthcareProfessionalVo> queryResults = healthcareProfessionalRepository
                .findAllByInstitution(institutionId);
        List<HealthcareProfessionalBo> result = new ArrayList<>();
        queryResults.forEach(hcp ->
            result.add(new HealthcareProfessionalBo(hcp))
        );
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public HealthcareProfessionalGroup addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId) {
        LOG.debug("Input parameters -> internmentEpisode {}, healthcareProfessionalId {}", internmentEpisodeId, healthcareProfessionalId);
        HealthcareProfessionalGroup healthcareProfessionalGroupToSave = new HealthcareProfessionalGroup(internmentEpisodeId, healthcareProfessionalId);
        healthcareProfessionalGroupToSave.setResponsible(true);
        HealthcareProfessionalGroup result = healthcareProfessionalGroupRepository.save(healthcareProfessionalGroupToSave);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Integer getProfessionalId(Integer userId) {
        LOG.debug("Input parameters -> userId {}", userId);
        Integer result = healthcareProfessionalRepository.getProfessionalId(userId);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public HealthcareProfessionalBo findProfessionalById(Integer id) {
        LOG.debug("Input parameters -> id {}", id);
        HealthcareProfessionalBo result = healthcareProfessionalRepository.findProfessionalById(id)
                .map(HealthcareProfessionalBo::new).orElseThrow(() -> new NotFoundException("id", "Professional " + id + " does not exist"));
        LOG.debug(OUTPUT, result);
        return result;
    }


}
