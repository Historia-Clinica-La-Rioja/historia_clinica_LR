package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.DepartmentInstitutionDto;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
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
public class BackofficeDepartmentInstitutionsStore implements BackofficeStore<DepartmentInstitutionDto, Integer> {

	private final InstitutionRepository repository;

	public BackofficeDepartmentInstitutionsStore (InstitutionRepository institutionRepository){
		this.repository = institutionRepository;
	}

	@Override
	public Page<DepartmentInstitutionDto> findAll(DepartmentInstitutionDto example, Pageable pageable) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Institution institutionExample = new Institution();
		institutionExample.setName(example.getName());
		List<Institution> institutions = repository.findAll(Example.of(institutionExample, customExampleMatcher), pageable.getSort());
		if (example.getDepartmentId() != null) {
			List<Integer> departmentInstitutionIds = repository.findByDepartmentId(example.getDepartmentId()).stream().map(InstitutionBasicInfoBo::getId).collect(Collectors.toList());
			institutions = institutions.stream().filter(institution -> departmentInstitutionIds.contains(institution.getId())).collect(Collectors.toList());
		} else if (example.getProvinceId() != null) {
			List<Integer> provinceInstitutionsIds = repository.findByProvinceId(example.getProvinceId()).stream().map(InstitutionBasicInfoBo::getId).collect(Collectors.toList());
			institutions = institutions.stream().filter(institution -> provinceInstitutionsIds.contains(institution.getId())).collect(Collectors.toList());
		}
		List<DepartmentInstitutionDto> result = institutions.stream().map(DepartmentInstitutionDto::new).collect(Collectors.toList());
		return new PageImpl<>(result, pageable, result.size());
	}

	@Override
	public List<DepartmentInstitutionDto> findAll() {
		return Collections.emptyList();
	}

	@Override
	public List<DepartmentInstitutionDto> findAllById(List<Integer> ids) {
		return Collections.emptyList();
	}

	@Override
	public Optional<DepartmentInstitutionDto> findById(Integer id) {
		var institution = repository.findById(id);
		if (institution.isPresent()){
			DepartmentInstitutionDto result = new DepartmentInstitutionDto();
			result.setId(institution.get().getId());
			result.setName(institution.get().getName());
			return Optional.of(result);
		}
		return Optional.empty();
	}

	@Override
	public DepartmentInstitutionDto save(DepartmentInstitutionDto entity) {
		return null;
	}

	@Override
	public void deleteById(Integer id) {

	}

	@Override
	public Example<DepartmentInstitutionDto> buildExample(DepartmentInstitutionDto entity) {
		return Example.of(entity);
	}
}
