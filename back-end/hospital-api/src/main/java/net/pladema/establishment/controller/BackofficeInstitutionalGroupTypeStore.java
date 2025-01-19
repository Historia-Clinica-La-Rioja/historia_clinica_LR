package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.EInstitutionalGroupType;
import net.pladema.establishment.controller.dto.InstitutionalGroupTypeDto;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BackofficeInstitutionalGroupTypeStore implements BackofficeStore<InstitutionalGroupTypeDto, Short> {

	@Override
	public Page<InstitutionalGroupTypeDto> findAll(InstitutionalGroupTypeDto example, Pageable pageable) {
		List<InstitutionalGroupTypeDto> result = getInstitutionsGroupType();
		return new PageImpl<>(result, pageable, result.size());
	}

	@Override
	public List<InstitutionalGroupTypeDto> findAll() {
		return getInstitutionsGroupType();
	}

	@Override
	public List<InstitutionalGroupTypeDto> findAllById(List<Short> ids) {
		return getInstitutionsGroupType().stream().filter(dto -> ids.contains(dto.getId())).collect(Collectors.toList());
	}

	@Override
	public Optional<InstitutionalGroupTypeDto> findById(Short id) {
		return getInstitutionsGroupType().stream().filter(dto -> dto.getId().equals(id)).findFirst();
	}

	@Override
	public InstitutionalGroupTypeDto save(InstitutionalGroupTypeDto entity) {
		return null;
	}

	@Override
	public void deleteById(Short id) {

	}

	@Override
	public Example<InstitutionalGroupTypeDto> buildExample(InstitutionalGroupTypeDto entity) {
		return null;
	}

	private List<InstitutionalGroupTypeDto> getInstitutionsGroupType() {
		return Stream.of(EInstitutionalGroupType.values())
				.map(institutionsGroupType -> new InstitutionalGroupTypeDto(institutionsGroupType.getId(), institutionsGroupType.getValue()))
				.collect(Collectors.toList());
	}

}
