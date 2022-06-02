package net.pladema.staff.controller.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;

@Service
public class BackofficeHealthcareProfessionalSpecialtyStore
		implements BackofficeStore <HealthcareProfessionalSpecialty, Integer> {

	private final HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository;

	public BackofficeHealthcareProfessionalSpecialtyStore(HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository){
		this.healthcareProfessionalSpecialtyRepository = healthcareProfessionalSpecialtyRepository;
	}

	@Override
	public Page<HealthcareProfessionalSpecialty> findAll(HealthcareProfessionalSpecialty entity, Pageable pageable) {

		ExampleMatcher customExampleMatcher = ExampleMatcher.matching().
				withMatcher("licenseNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		return healthcareProfessionalSpecialtyRepository.findAll(
				Example.of(entity, customExampleMatcher),
				PageRequest.of(
						pageable.getPageNumber(),
						pageable.getPageSize(),
						Sort.unsorted()
				)
		);
	}

	@Override
	public List<HealthcareProfessionalSpecialty> findAll() {
		return healthcareProfessionalSpecialtyRepository.findAll().stream().
				collect(Collectors.toList());
	}

	@Override
	public List<HealthcareProfessionalSpecialty> findAllById(List<Integer> ids) {
		return healthcareProfessionalSpecialtyRepository.findAllById(ids).stream()
				.collect(Collectors.toList());
	}

	@Override
	public Optional<HealthcareProfessionalSpecialty> findById(Integer id) {
		return healthcareProfessionalSpecialtyRepository.findById(id);
	}

	@Override
	public HealthcareProfessionalSpecialty save(HealthcareProfessionalSpecialty entity) {
		return healthcareProfessionalSpecialtyRepository.findByUniqueKey(entity.getProfessionalProfessionsId(), entity.getClinicalSpecialtyId()).
				map(healthcareProfessionalSpecialty -> {
					if (healthcareProfessionalSpecialty.isDeleted())
						healthcareProfessionalSpecialty.setDeleted(false);
					return healthcareProfessionalSpecialtyRepository.save(healthcareProfessionalSpecialty);
				}).orElseGet(()-> healthcareProfessionalSpecialtyRepository.save(entity));
	}

	@Override
	public void deleteById(Integer id) {
		healthcareProfessionalSpecialtyRepository.deleteById(id);
	}

	@Override
	public Example<HealthcareProfessionalSpecialty> buildExample(HealthcareProfessionalSpecialty entity) {
		return null;
	}

}
