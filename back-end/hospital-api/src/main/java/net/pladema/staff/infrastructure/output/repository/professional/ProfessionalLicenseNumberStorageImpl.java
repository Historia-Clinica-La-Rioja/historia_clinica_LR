package net.pladema.staff.infrastructure.output.repository.professional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.RoleUtils;
import net.pladema.staff.application.ports.ProfessionalLicenseNumberStorage;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;
import net.pladema.staff.domain.ProfessionalRegistrationNumbersBo;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.ProfessionalLicenseNumberRepository;
import net.pladema.staff.repository.domain.BasicPersonalDataVo;
import net.pladema.staff.repository.domain.ProfessionalRegistrationNumberVo;
import net.pladema.staff.repository.entity.ProfessionalLicenseNumber;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessionalLicenseNumberStorageImpl implements ProfessionalLicenseNumberStorage {

	private final ProfessionalLicenseNumberRepository repository;

	private final HealthcareProfessionalSpecialtyRepository hpsRepository;

	@Override
	public List<ProfessionalLicenseNumberBo> getByHealthCareProfessionalId(Integer healthcareProfessionalId) {
		log.debug("Inputs parameters -> healthcareProfessionalId {}", healthcareProfessionalId);
		List<ProfessionalLicenseNumberBo> result = repository.findByHealthcareProfessionalId(healthcareProfessionalId).stream().map(this::mapToBo).collect(Collectors.toList());
		result.forEach(l-> {
			if(l.getProfessionalProfessionId()==null&&l.getHealthcareProfessionalSpecialtyId()!=null)
				l.setProfessionalProfessionId(hpsRepository.findById(l.getHealthcareProfessionalSpecialtyId()).get().getProfessionalProfessionId());
		});
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public void save(ProfessionalLicenseNumberBo professionalLicenseNumberBo) {
		log.debug("Inputs parameters -> professionalLicenseNumberBo {}", professionalLicenseNumberBo);
		repository.save(mapToEntity(professionalLicenseNumberBo));
	}

	@Override
	public void delete(Integer id) {
		log.debug("Inputs parameters -> professionalLicenseNumberId {}", id);
		repository.deleteById(id);
	}

	@Override
	public List<ProfessionalRegistrationNumbersBo> getAllProfessionalRegistrationNumbersBo(Integer institutionId) {
		log.debug("Inputs parameters -> institutionId {}", institutionId);
		List<Short> professionalERolIds = RoleUtils.getProfessionalERoleIds();
		List<ProfessionalRegistrationNumberVo> registrationNumbers = repository.findAllProfessionalRegistrationNumbers(institutionId, professionalERolIds);

		Map<Integer, List<ProfessionalRegistrationNumberVo>> personLicensesMapping = new HashMap<>();
		registrationNumbers.forEach(prn -> {
			Integer healthcareProfessionalId = prn.getHealthcareProfessionalId();
			personLicensesMapping.computeIfAbsent(healthcareProfessionalId, key -> new ArrayList<>()).add(prn);
		});

		List<ProfessionalRegistrationNumbersBo> result = personLicensesMapping.keySet().stream()
			.map(healthcareProfessionalId -> {
				List<ProfessionalRegistrationNumberVo> entities = personLicensesMapping.get(healthcareProfessionalId);
				List<ProfessionalLicenseNumberBo> license = entities.stream()
						.map(ProfessionalRegistrationNumberVo::getProfessionalLicenseNumber)
						.filter(Objects::nonNull)
						.map(this::mapToBo)
						.sorted(Comparator.comparing(o -> o.getType().getId()))
						.collect(Collectors.toList());

				BasicPersonalDataVo bpd = entities.get(0).getBasicPersonalDataVo();
				return new ProfessionalRegistrationNumbersBo(entities.get(0).getHealthcareProfessionalId(),
						bpd.getFirstName(), bpd.getLastName(), bpd.getNameSelfDetermination(), license);
			})
			.collect(Collectors.toList());

		log.debug("Output -> {}", result);
		return result;
	}

	private ProfessionalLicenseNumberBo mapToBo(ProfessionalLicenseNumber entity) {
		return new ProfessionalLicenseNumberBo(entity.getId(),
				entity.getLicenseNumber(),
				entity.getType(),
				entity.getProfessionalProfessionId(),
				entity.getHealthcareProfessionalSpecialtyId());
	}

	private ProfessionalLicenseNumber mapToEntity(ProfessionalLicenseNumberBo bo){
		return new ProfessionalLicenseNumber(bo.getId(),
				bo.getLicenseNumber(),
				bo.getType(),
				bo.getProfessionalProfessionId(),
				bo.getHealthcareProfessionalSpecialtyId());
	}
}
