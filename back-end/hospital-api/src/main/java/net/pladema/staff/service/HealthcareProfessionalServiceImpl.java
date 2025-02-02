package net.pladema.staff.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import net.pladema.permissions.RoleUtils;
import net.pladema.permissions.repository.enums.ERole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.clinichistory.hospitalization.repository.HealthcareProfessionalGroupRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import net.pladema.staff.service.domain.HealthcareProfessionalCompleteBo;
import net.pladema.staff.service.exceptions.HealthcareProfessionalEnumException;
import net.pladema.staff.service.exceptions.HealthcareProfessionalException;
import static java.util.List.of;

@Service
public class HealthcareProfessionalServiceImpl implements  HealthcareProfessionalService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";

    private final HealthcareProfessionalGroupRepository healthcareProfessionalGroupRepository;

    private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	private final SharedPersonPort sharedPersonPort;

    public HealthcareProfessionalServiceImpl(HealthcareProfessionalGroupRepository healthcareProfessionalGroupRepository,
                                             HealthcareProfessionalRepository healthcareProfessionalRepository,
											 SharedPersonPort sharedPersonPort) {
        this.healthcareProfessionalGroupRepository = healthcareProfessionalGroupRepository;
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
		this.sharedPersonPort = sharedPersonPort;
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
        List<HealthcarePersonBo> result = healthcareProfessionalRepository.getAllDoctors(institutionId, List.of(ERole.ESPECIALISTA_MEDICO.getId(), ERole.ESPECIALISTA_EN_ODONTOLOGIA.getId()));
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<HealthcareProfessionalBo> getAllByInstitution(Integer institutionId) {
        LOG.debug("Input parameters -> institutionId {}", institutionId);
        List<Short> professionalERolIds = RoleUtils.getProfessionalERoleIds();
        List<HealthcareProfessionalVo> queryResults = healthcareProfessionalRepository
                .findAllByInstitution(institutionId, professionalERolIds);
		List<HealthcareProfessionalBo> result = queryResults.stream()
				.map(this::mapToHealthcareProfessionalBo)
				.collect(Collectors.toList());
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
    public Integer getUserIdHealthcareProfessionalId(Integer healthcareProfessionalId) {
        LOG.debug("Input parameters -> healthcareProfessionalId {}", healthcareProfessionalId);
        Integer result = healthcareProfessionalRepository.getUserIdByHealthcareProfessionalId(healthcareProfessionalId);
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
    public HealthcareProfessionalBo findActiveProfessionalById(Integer id) {
        LOG.debug("Input parameters -> id {}", id);
        HealthcareProfessionalBo result = healthcareProfessionalRepository.findActiveProfessionalById(id)
                .map(HealthcareProfessionalBo::new).orElseThrow(() -> new NotFoundException("id", "Professional " + id + " does not exist"));
        LOG.debug(OUTPUT, result);
        return result;
    }

	@Override
	public HealthcareProfessionalBo findFromAllProfessionalsById(Integer healthcareProfessionalId) {
		LOG.debug("Input parameters -> id {}", healthcareProfessionalId);
		HealthcareProfessionalBo result = healthcareProfessionalRepository.findFromAllProfessionalsById(healthcareProfessionalId)
				.map(HealthcareProfessionalBo::new).orElseThrow(() -> new NotFoundException("id", "Professional " + healthcareProfessionalId + " does not exist"));
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
    public HealthcareProfessionalBo findProfessionalByPersonId(Integer personId){
        LOG.debug("Input parameters -> personId {}", personId);
        return healthcareProfessionalRepository.findProfessionalByPersonId(personId)
                .map(professionalId -> healthcareProfessionalRepository.findActiveProfessionalById(professionalId)
                        .map(HealthcareProfessionalBo::new).orElse(null))
                .orElse(null);
    }

	@Override
	public HealthcareProfessionalBo findProfessionalByUserId(Integer userId){
		LOG.debug("Input parameters -> personId {}", userId);
		Integer professionalId = healthcareProfessionalRepository.getProfessionalId(userId);
		HealthcareProfessionalBo result = healthcareProfessionalRepository.findActiveProfessionalById(professionalId)
						.map(HealthcareProfessionalBo::new).orElse(null);
		LOG.debug(OUTPUT, result);
		return result;
	}

    @Override
    public Integer saveProfessional(HealthcareProfessionalCompleteBo professionalBo){
        LOG.debug("Input parameters -> professionalBo {}", professionalBo);
        Integer result =(professionalBo.getId()==null)  ?create(professionalBo) : update(professionalBo);
        LOG.debug(OUTPUT,result);
        return result;
    }

	@Override
	public Optional<Integer> getProfessionalIdByPersonId(Integer personId) {
		LOG.debug("Input parameters -> personId {}", personId);
		Optional<Integer> result = healthcareProfessionalRepository.findByPersonId(personId).map(HealthcareProfessional::getId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<HealthcareProfessionalBo> getVirtualConsultationProfessionalsByInstitutionId(Integer institutionId) {
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		List<HealthcareProfessionalBo> result = healthcareProfessionalRepository.getVirtualConsultationProfessionalsByInstitutionId(institutionId).stream().map(HealthcareProfessionalBo::new).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<HealthcareProfessionalBo> getVirtualConsultationResponsiblesByInstitutionId(Integer institutionId) {
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		List<Short> professionalERolIds = of(ERole.VIRTUAL_CONSULTATION_RESPONSIBLE.getId());
		List<HealthcareProfessionalVo> queryResults = healthcareProfessionalRepository
				.findAllByInstitution(institutionId, professionalERolIds);
		List<HealthcareProfessionalBo> result = new ArrayList<>();
		queryResults.forEach(hcp ->
				result.add(new HealthcareProfessionalBo(hcp))
		);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<HealthcareProfessionalBo> getAllProfessionalsByDepartment(Short departmentId) {
		LOG.debug("Input parameters -> departmentId {}", departmentId);
		List<Short> professionalERolIds = RoleUtils.getProfessionalERoleIds();
		List<HealthcareProfessionalVo> professionals = healthcareProfessionalRepository.getAllProfessionalsByDepartment(departmentId, professionalERolIds);
		List<HealthcareProfessionalBo> result = professionals.stream()
				.map(this::mapToHealthcareProfessionalBo)
				.collect(Collectors.toList());
		LOG.debug("Output result -> {} ", result);
		return result;
	}

	private Integer update(HealthcareProfessionalCompleteBo professionalCompleteBo){
        HealthcareProfessional result = healthcareProfessionalRepository.findById(professionalCompleteBo.getId())
                .map(healthcareProfessionalRepository::save).orElseThrow(()->new HealthcareProfessionalException(HealthcareProfessionalEnumException.HEALTHCARE_PROFESSIONAL_NOT_FOUND,"El profesional no existe"));
        return result.getId();
    }

    private Integer create (HealthcareProfessionalCompleteBo professionalCompleteBo){
        HealthcareProfessional saved = healthcareProfessionalRepository.save(new HealthcareProfessional(
                professionalCompleteBo.getPersonId()));
        Integer result = saved.getId();
        return result;
    }

	private HealthcareProfessionalBo mapToHealthcareProfessionalBo(HealthcareProfessionalVo hcp) {
		String completePersonName = sharedPersonPort.parseCompletePersonName(
				hcp.getFirstName(),
				hcp.getMiddleNames(),
				hcp.getLastName(),
				hcp.getOtherLastNames(),
				hcp.getNameSelfDetermination()
		);
		return new HealthcareProfessionalBo(hcp, completePersonName);
	}

}
