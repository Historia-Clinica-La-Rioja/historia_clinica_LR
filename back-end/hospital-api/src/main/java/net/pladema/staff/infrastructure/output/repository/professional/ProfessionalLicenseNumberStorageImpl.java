package net.pladema.staff.infrastructure.output.repository.professional;

import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	public List<ProfessionalLicenseNumberBo> getByHealthCareProfessionalId(Integer healthcareProfessionalId) {
		log.debug("Inputs parameters -> healthcareProfessionalId {}", healthcareProfessionalId);
		List<ProfessionalLicenseNumberBo> result = repository.findByHealthcareProfessionalId(healthcareProfessionalId).stream().map(this::mapToBo).collect(Collectors.toList());
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
}
