package net.pladema.staff.service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.clinichistory.hospitalization.repository.HealthcareProfessionalGroupRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import net.pladema.staff.service.domain.HealthcareProfessionalCompleteBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<HealthcareProfessionalBo> getAllProfessional() {
        LOG.debug("getAllProfessional");
        List<HealthcareProfessionalBo> result =
                healthcareProfessionalRepository.getAllProfessional()
                .stream().map(HealthcareProfessionalBo::new)
                        .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<HealthcarePersonBo> getAllDoctorsByInstitution(Integer institutionId) {
        LOG.debug("Input parameters -> institutionId {}", institutionId);
        List<HealthcarePersonBo> result = healthcareProfessionalRepository.getAllDoctors(institutionId);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<HealthcareProfessionalBo> getAllByInstitution(Integer institutionId) {
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

    @Override
    public HealthcareProfessionalBo findProfessionalByPersonId(Integer personId){
        LOG.debug("Input parameters -> personId {}", personId);
        return healthcareProfessionalRepository.findProfessionalByPersonId(personId)
                .map(professionalId -> healthcareProfessionalRepository.findProfessionalById(professionalId)
                        .map(HealthcareProfessionalBo::new).orElse(null))
                .orElse(null);
    }

    @Override
    public Integer saveProfessional(HealthcareProfessionalCompleteBo professionalBo){
        LOG.debug("Input parameters -> professionalBo {}", professionalBo);
        HealthcareProfessional saved = healthcareProfessionalRepository.save(new HealthcareProfessional(
                professionalBo.getLicenseNumber(),
                professionalBo.getPersonId()
        ));
        Integer result = saved.getId();
        LOG.debug(OUTPUT,result);
        return result;
    }
}
