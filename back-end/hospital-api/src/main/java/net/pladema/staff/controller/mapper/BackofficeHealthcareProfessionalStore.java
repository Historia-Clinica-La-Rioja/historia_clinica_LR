package net.pladema.staff.controller.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.pladema.staff.repository.VAvailableProfessionalRepository;

import net.pladema.staff.repository.entity.VAvailableProfessional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.controller.dto.BackofficeHealthcareProfessionalCompleteDto;

@Service
public class BackofficeHealthcareProfessionalStore implements BackofficeStore<BackofficeHealthcareProfessionalCompleteDto, Integer> {

	private final VAvailableProfessionalRepository vAvailableProfessionalRepository;

	public BackofficeHealthcareProfessionalStore(VAvailableProfessionalRepository vAvailableProfessionalRepository) {
		this.vAvailableProfessionalRepository = vAvailableProfessionalRepository;
	}

	@Override
	public Page<BackofficeHealthcareProfessionalCompleteDto> findAll(BackofficeHealthcareProfessionalCompleteDto example, Pageable pageable) {
		VAvailableProfessional hp = buildHealthcareProfessionalEntity(example);
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withMatcher("firstName", ExampleMatcher.GenericPropertyMatcher::contains);
		return vAvailableProfessionalRepository.findAll(
				Example.of(hp, matcher),
				PageRequest.of(
						pageable.getPageNumber(),
						pageable.getPageSize(),
						Sort.unsorted()
				)
		).map(this::buildHealthcareProfessionalDto);
	}

	@Override
	public List<BackofficeHealthcareProfessionalCompleteDto> findAll() {
		return vAvailableProfessionalRepository.findAll().stream()
				.map(this::buildHealthcareProfessionalDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<BackofficeHealthcareProfessionalCompleteDto> findAllById(List<Integer> ids) {
		return vAvailableProfessionalRepository.findAllById(ids).stream().map(this::buildHealthcareProfessionalDto).collect(Collectors.toList());
	}

	@Override
	public Optional<BackofficeHealthcareProfessionalCompleteDto> findById(Integer id) {
		return toHealthcareProfessionalCreateDto(vAvailableProfessionalRepository.findById(id));
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

	private Optional<BackofficeHealthcareProfessionalCompleteDto> toHealthcareProfessionalCreateDto(Optional<VAvailableProfessional> byId) {
		return byId.map(this::buildHealthcareProfessionalDto);
	}

	private BackofficeHealthcareProfessionalCompleteDto buildHealthcareProfessionalDto(VAvailableProfessional byId) {
		BackofficeHealthcareProfessionalCompleteDto dto = new BackofficeHealthcareProfessionalCompleteDto();
		dto.setId(byId.getId());
		dto.setPersonId(byId.getPersonId());
		dto.setFirstName(byId.getFirstName() != null ? byId.getFirstName().toUpperCase() : null);
		dto.setLastName(byId.getLastName() != null ? byId.getLastName().toUpperCase(): null);
		dto.setIdentificationNumber(byId.getIdentificationNumber());
		dto.setIdentificationTypeId(byId.getIdentificationTypeId());
		return dto;
	}

	private VAvailableProfessional buildHealthcareProfessionalEntity(BackofficeHealthcareProfessionalCompleteDto dto){
		VAvailableProfessional hp = new VAvailableProfessional();
		hp.setId(dto.getId());
		hp.setPersonId(dto.getPersonId());
		hp.setFirstName(dto.getFirstName() != null ? dto.getFirstName().toUpperCase() : null);
		hp.setLastName(dto.getLastName() != null ? dto.getLastName().toUpperCase(): null);
		hp.setIdentificationNumber(dto.getIdentificationNumber());
		hp.setIdentificationTypeId(dto.getIdentificationTypeId());
		return hp;
	}
}
