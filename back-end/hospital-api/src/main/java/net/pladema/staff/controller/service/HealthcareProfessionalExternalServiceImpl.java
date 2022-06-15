package net.pladema.staff.controller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import net.pladema.clinichistory.hospitalization.controller.dto.HealthCareProfessionalGroupDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.HealthCareProfessionalGroupMapper;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.staff.application.fetchprofessionalbyid.FetchProfessionalById;
import net.pladema.staff.application.fetchprofessionalbyuser.FetchProfessionalByUser;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

@Service
public class HealthcareProfessionalExternalServiceImpl implements HealthcareProfessionalExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalExternalServiceImpl.class);

    private final HealthcareProfessionalService healthcareProfessionalService;

    private final HealthCareProfessionalGroupMapper healthCareProfessionalGroupMapper;

    private final HealthcareProfessionalMapper healthcareProfessionalMapper;

	private final FetchProfessionalByUser fetchProfessionalByUser;

	private final FetchProfessionalById fetchProfessionalById;
    public HealthcareProfessionalExternalServiceImpl(HealthcareProfessionalService healthcareProfessionalService,
													 HealthCareProfessionalGroupMapper healthCareProfessionalGroupMapper,
													 HealthcareProfessionalMapper healthcareProfessionalMapper,
													 FetchProfessionalByUser fetchProfessionalByUser,
													 FetchProfessionalById fetchProfessionalById){
        this.healthcareProfessionalService = healthcareProfessionalService;
        this.healthCareProfessionalGroupMapper = healthCareProfessionalGroupMapper;
        this.healthcareProfessionalMapper = healthcareProfessionalMapper;
		this.fetchProfessionalByUser = fetchProfessionalByUser;
		this.fetchProfessionalById = fetchProfessionalById;
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
    public ProfessionalDto findActiveProfessionalById(Integer healthCareProfessionalId) {
        LOG.debug("Input parameters -> healthCareProfessionalId {}", healthCareProfessionalId);
        HealthcareProfessionalBo healthcareProfessionalBo = healthcareProfessionalService.findActiveProfessionalById(healthCareProfessionalId);
        ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(healthcareProfessionalBo);
        LOG.debug("Output -> {}", result);
        return result;
    }

	@Override
	public ProfessionalDto findFromAllProfessionalsById(Integer healthCareProfessionalId) {
		LOG.debug("Input parameters -> healthCareProfessionalId {}", healthCareProfessionalId);
		HealthcareProfessionalBo healthcareProfessionalBo = healthcareProfessionalService.findFromAllProfessionalsById(healthCareProfessionalId);
		ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(healthcareProfessionalBo);
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
    public ProfessionalDto findProfessionalByUserId(Integer userId) {
        LOG.debug("Input parameters -> userId {}", userId);
		Integer professionalId = healthcareProfessionalService.getProfessionalId(userId);
        HealthcareProfessionalBo healthcareProfessionalBo = healthcareProfessionalService.findActiveProfessionalById(professionalId);
        ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(healthcareProfessionalBo);
        LOG.debug("Output -> {}", result);
        return result;
    }

	@Override
	public ProfessionalCompleteDto getProfessionalCompleteInfoByUser(Integer userId) {
		return healthcareProfessionalMapper.fromProfessionalCompleteBo(fetchProfessionalByUser.execute(userId));
	}

	@Override
	public ProfessionalCompleteDto getProfessionalCompleteInfoById(Integer professionalId) {
		return healthcareProfessionalMapper.fromProfessionalCompleteBo(fetchProfessionalById.execute(professionalId));
	}

}
