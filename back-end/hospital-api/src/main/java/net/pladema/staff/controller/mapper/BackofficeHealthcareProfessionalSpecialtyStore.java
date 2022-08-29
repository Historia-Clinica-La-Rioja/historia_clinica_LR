package net.pladema.staff.controller.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
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

		entity.setDeleted(false);
		return healthcareProfessionalSpecialtyRepository.findAll(
				Example.of(entity),
				PageRequest.of(
						pageable.getPageNumber(),
						pageable.getPageSize(),
						Sort.unsorted()
				)
		);
	}

	@Override
	public List<HealthcareProfessionalSpecialty> findAll() {
		return healthcareProfessionalSpecialtyRepository.findAll().stream()
				.filter(healthcareProfessionalSpecialty -> !healthcareProfessionalSpecialty.isDeleted())
				.collect(Collectors.toList());
	}

	@Override
	public List<HealthcareProfessionalSpecialty> findAllById(List<Integer> ids) {
		return healthcareProfessionalSpecialtyRepository.findAllById(ids).stream()
				.filter(healthcareProfessionalSpecialty -> !healthcareProfessionalSpecialty.isDeleted())
				.collect(Collectors.toList());
	}

	@Override
	public Optional<HealthcareProfessionalSpecialty> findById(Integer id) {
		return healthcareProfessionalSpecialtyRepository.findById(id);
	}

	@Override
	public HealthcareProfessionalSpecialty save(HealthcareProfessionalSpecialty entity) {
		return healthcareProfessionalSpecialtyRepository
				.findByUniqueKey(entity.getProfessionalProfessionId(), entity.getClinicalSpecialtyId())
				.map(healthcareProfessionalSpecialtyRepository::reactivate)
				.orElseGet( () -> healthcareProfessionalSpecialtyRepository.save(entity));
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
