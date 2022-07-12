package net.pladema.staff.controller.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.controller.dto.BackofficeHealthcareProfessionalCompleteDto;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;

@Service
public class BackofficeHealthcareProfessionalStore implements BackofficeStore<BackofficeHealthcareProfessionalCompleteDto, Integer> {

	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	public BackofficeHealthcareProfessionalStore(HealthcareProfessionalRepository healthcareProfessionalRepository) {
		this.healthcareProfessionalRepository = healthcareProfessionalRepository;
	}

	@Override
	public Page<BackofficeHealthcareProfessionalCompleteDto> findAll(BackofficeHealthcareProfessionalCompleteDto example, Pageable pageable) {
		return null;
	}

	@Override
	public List<BackofficeHealthcareProfessionalCompleteDto> findAll() {
		return null;
	}

	@Override
	public List<BackofficeHealthcareProfessionalCompleteDto> findAllById(List<Integer> ids) {
		return healthcareProfessionalRepository.findAllById(ids).stream().map(this::buildHealthcareProfessionalDto).collect(Collectors.toList());
	}

	@Override
	public Optional<BackofficeHealthcareProfessionalCompleteDto> findById(Integer id) {
		return toHealthcareProfessionalCreateDto(healthcareProfessionalRepository.findById(id));
	}

	@Override
	public BackofficeHealthcareProfessionalCompleteDto save(BackofficeHealthcareProfessionalCompleteDto dto) {
		return null;
	}

	@Override
	public void deleteById(Integer id) {
	}

	@Override
	public Example<BackofficeHealthcareProfessionalCompleteDto> buildExample(BackofficeHealthcareProfessionalCompleteDto entity) {
		return null;
	}

	private Optional<BackofficeHealthcareProfessionalCompleteDto> toHealthcareProfessionalCreateDto(Optional<HealthcareProfessional> byId) {
		return byId.map(this::buildHealthcareProfessionalDto);
	}

	private BackofficeHealthcareProfessionalCompleteDto buildHealthcareProfessionalDto(HealthcareProfessional byId) {
		BackofficeHealthcareProfessionalCompleteDto dto = new BackofficeHealthcareProfessionalCompleteDto();
		dto.setPersonId(byId.getPersonId());
		dto.setId(byId.getId());
		if (byId.getLicenseNumber().isBlank()) dto.setLicenseNumber("Número de matrícula no definido");
		else dto.setLicenseNumber(byId.getLicenseNumber());
		return dto;
	}

}
