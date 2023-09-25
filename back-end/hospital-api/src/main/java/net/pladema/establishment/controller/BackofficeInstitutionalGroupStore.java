package net.pladema.establishment.controller;

import com.google.common.base.Joiner;

import net.pladema.establishment.controller.dto.InstitutionalGroupDto;
import net.pladema.establishment.repository.InstitutionalGroupInstitutionRepository;
import net.pladema.establishment.repository.InstitutionalGroupRepository;
import net.pladema.establishment.repository.entity.InstitutionalGroup;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeInstitutionalGroupStore implements BackofficeStore<InstitutionalGroupDto, Integer> {

	private final InstitutionalGroupRepository repository;
	private final InstitutionalGroupInstitutionRepository institutionsGroupInstitutionRepository;

	public BackofficeInstitutionalGroupStore(InstitutionalGroupRepository repository, InstitutionalGroupInstitutionRepository institutionsGroupInstitutionRepository){
		this.repository = repository;
		this.institutionsGroupInstitutionRepository = institutionsGroupInstitutionRepository;
	}

	@Override
	public Page<InstitutionalGroupDto> findAll(InstitutionalGroupDto example, Pageable pageable) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		List<InstitutionalGroupDto> list = repository.findAll(Example.of(new InstitutionalGroup(example), customExampleMatcher), pageable)
				.map(this::mapToDto)
				.toList();
		long totalElements = repository.count();
		return new PageImpl<>(list, pageable, list.isEmpty() ? 0 : totalElements);
	}

	@Override
	public List<InstitutionalGroupDto> findAll() {
		return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public List<InstitutionalGroupDto> findAllById(List<Integer> ids) {
		return repository.findAllById(ids).stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public Optional<InstitutionalGroupDto> findById(Integer id) {
		return repository.findById(id).map(this::mapToDto);
	}

	@Override
	public InstitutionalGroupDto save(InstitutionalGroupDto entity) {
		InstitutionalGroup entityToSave = new InstitutionalGroup();
		entityToSave.setTypeId(entity.getTypeId());
		entityToSave.setName(entity.getName());
		entity.setId(repository.save(entityToSave).getId());
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
		institutionsGroupInstitutionRepository.deleteByInstitutionalGroupId(id);
		repository.deleteById(id);
	}

	@Override
	public Example<InstitutionalGroupDto> buildExample(InstitutionalGroupDto entity) {
		return Example.of(entity);
	}

	private InstitutionalGroupDto mapToDto (InstitutionalGroup entity){
		InstitutionalGroupDto result = new InstitutionalGroupDto();
		List<String> institutionNames = repository.getInstitutionsNamesById(entity.getId());
		result.setId(entity.getId());
		result.setName(entity.getName());
		result.setTypeId(entity.getTypeId());
		result.setInstitutions(Joiner.on(", ").join(institutionNames));
		return result;
	}

}
