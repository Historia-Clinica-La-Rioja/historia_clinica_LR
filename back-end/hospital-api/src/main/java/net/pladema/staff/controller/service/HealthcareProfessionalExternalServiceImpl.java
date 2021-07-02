package net.pladema.staff.controller.service;

import net.pladema.clinichistory.hospitalization.controller.dto.HealthCareProfessionalGroupDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.HealthCareProfessionalGroupMapper;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HealthcareProfessionalExternalServiceImpl implements HealthcareProfessionalExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalExternalServiceImpl.class);

    private final HealthcareProfessionalService healthcareProfessionalService;

    private final HealthCareProfessionalGroupMapper healthCareProfessionalGroupMapper;

    private final HealthcareProfessionalMapper healthcareProfessionalMapper;


    public HealthcareProfessionalExternalServiceImpl(HealthcareProfessionalService healthcareProfessionalService,
                                                     HealthCareProfessionalGroupMapper healthCareProfessionalGroupMapper,
                                                     HealthcareProfessionalMapper healthcareProfessionalMapper){
        this.healthcareProfessionalService = healthcareProfessionalService;
        this.healthCareProfessionalGroupMapper = healthCareProfessionalGroupMapper;
        this.healthcareProfessionalMapper = healthcareProfessionalMapper;
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

    @Override
    public ProfessionalDto findProfessionalById(Integer healthCareProfessionalId) {
        LOG.debug("Input parameters -> healthCareProfessionalId {}", healthCareProfessionalId);
        HealthcareProfessionalBo healthcareProfessionalBo = healthcareProfessionalService.findProfessionalById(healthCareProfessionalId);
        ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(healthcareProfessionalBo);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public ProfessionalDto findProfessionalByUserId(Integer userId) {
        LOG.debug("Input parameters -> userId {}", userId);
        Integer professionalId = healthcareProfessionalService.getProfessionalId(userId);
        HealthcareProfessionalBo healthcareProfessionalBo = healthcareProfessionalService.findProfessionalById(professionalId);
        ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(healthcareProfessionalBo);
        LOG.debug("Output -> {}", result);
        return result;
    }


}
