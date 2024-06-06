package net.pladema.staff.controller.service;

import java.util.List;

import net.pladema.person.controller.service.PersonExternalService;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.MedicineDoctorCompleteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.controller.dto.HealthCareProfessionalGroupDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.HealthCareProfessionalGroupMapper;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.person.service.PersonService;
import net.pladema.staff.application.FetchProfessionalsByIds;
import net.pladema.staff.application.fetchprofessionalbyid.FetchProfessionalById;
import net.pladema.staff.application.fetchprofessionalbyuser.FetchProfessionalByUser;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

@Slf4j
@RequiredArgsConstructor
@Service
public class HealthcareProfessionalExternalServiceImpl implements HealthcareProfessionalExternalService {

    private final HealthcareProfessionalService healthcareProfessionalService;

    private final HealthCareProfessionalGroupMapper healthCareProfessionalGroupMapper;

    private final HealthcareProfessionalMapper healthcareProfessionalMapper;

	private final FetchProfessionalByUser fetchProfessionalByUser;

	private final FetchProfessionalById fetchProfessionalById;

	private final FetchProfessionalsByIds fetchProfessionalsByIds;

	private final PersonExternalService personExternalService;

    @Override
    public HealthCareProfessionalGroupDto addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId) {
        log.debug("Input parameters -> internmentEpisode {}, healthcareProfessionalId {}", internmentEpisodeId, healthcareProfessionalId);
        HealthcareProfessionalGroup resultService = healthcareProfessionalService.addHealthcareProfessionalGroup(internmentEpisodeId, healthcareProfessionalId);
        HealthCareProfessionalGroupDto result = healthCareProfessionalGroupMapper.toHealthcareProfessionalGroupDto(resultService);
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Integer getProfessionalId(Integer userId) {
        log.debug("Input parameters -> userId {}", userId);
        Integer result = healthcareProfessionalService.getProfessionalId(userId);
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public ProfessionalDto findActiveProfessionalById(Integer healthCareProfessionalId) {
        log.debug("Input parameters -> healthCareProfessionalId {}", healthCareProfessionalId);
        HealthcareProfessionalBo healthcareProfessionalBo = healthcareProfessionalService.findActiveProfessionalById(healthCareProfessionalId);
        ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(healthcareProfessionalBo);
        log.debug("Output -> {}", result);
        return result;
    }

	@Override
	public ProfessionalDto findFromAllProfessionalsById(Integer healthCareProfessionalId) {
		log.debug("Input parameters -> healthCareProfessionalId {}", healthCareProfessionalId);
		HealthcareProfessionalBo healthcareProfessionalBo = healthcareProfessionalService.findFromAllProfessionalsById(healthCareProfessionalId);
		ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(healthcareProfessionalBo);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
    public ProfessionalDto findProfessionalByUserId(Integer userId) {
        log.debug("Input parameters -> userId {}", userId);
		Integer professionalId = healthcareProfessionalService.getProfessionalId(userId);
        HealthcareProfessionalBo healthcareProfessionalBo = healthcareProfessionalService.findActiveProfessionalById(professionalId);
        ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(healthcareProfessionalBo);
        log.debug("Output -> {}", result);
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

	@Override
	public MedicineDoctorCompleteDto getDoctorsCompleteInfo(Integer professionalId) {
		var professionalInfo = healthcareProfessionalMapper
				.fromProfessionalCompleteBo(fetchProfessionalById.execute(professionalId));
		var person = personExternalService.getBasicDataPerson(professionalInfo.getPersonId());

		return MedicineDoctorCompleteDto.builder()
				.firstName(person.getFirstName())
				.middleNames(person.getMiddleNames())
				.lastName(person.getLastName())
				.nameSelfDetermination(person.getNameSelfDetermination())
				.otherLastNames(person.getOtherLastNames())
				.identificationType(person.getIdentificationType())
				.identificationNumber(person.getIdentificationNumber())
				.professions(professionalInfo.getProfessions())
				.build();
	}

	@Override
	public List<ProfessionalCompleteDto> getProfessionalsCompleteInfoByIds(List<Integer> professionalIds) {
		return healthcareProfessionalMapper.fromProfessionalCompleteBoList(fetchProfessionalsByIds.execute(professionalIds));
	}

}
