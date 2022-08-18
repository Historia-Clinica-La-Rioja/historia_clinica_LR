package net.pladema.staff.infrastructure.output.repository.professional;

import java.util.List;
import java.util.stream.Collectors;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.ports.ProfessionalLicenseNumberStorage;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;
import net.pladema.staff.repository.ProfessionalLicenseNumberRepository;
import net.pladema.staff.repository.entity.ProfessionalLicenseNumber;

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
