package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.InstitutionalGroupInstitutionDto;
import net.pladema.establishment.repository.InstitutionalGroupInstitutionRepository;
import net.pladema.establishment.repository.domain.InstitutionalGroupInstitutionVo;
import net.pladema.establishment.repository.entity.InstitutionalGroupInstitution;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeInstitutionalGroupInstitutionStore implements BackofficeStore<InstitutionalGroupInstitutionDto, Integer> {

	private final InstitutionalGroupInstitutionRepository repository;

	public BackofficeInstitutionalGroupInstitutionStore(InstitutionalGroupInstitutionRepository repository){
		super();
		this.repository = repository;
	}
	@Override
	public Page<InstitutionalGroupInstitutionDto> findAll(InstitutionalGroupInstitutionDto example, Pageable pageable) {
		List<InstitutionalGroupInstitutionDto> result = repository.findByInstitutionalGroupId(example.getInstitutionalGroupId()).stream().map(this::mapToDto).collect(Collectors.toList());
		if (pageable.getSort().getOrderFor("institutionName") != null) {
			if (pageable.getSort().getOrderFor("institutionName").isDescending())
				result = result.stream().sorted(Comparator.comparing(dto -> dto.getInstitutionName().toLowerCase(), Comparator.reverseOrder())).collect(Collectors.toList());
			else
				result = result.stream().sorted(Comparator.comparing(dto -> dto.getInstitutionName().toLowerCase())).collect(Collectors.toList());
		}
		if (pageable.getSort().getOrderFor("departmentName") != null) {
			if (pageable.getSort().getOrderFor("departmentName").isDescending())
				result = result.stream().sorted(Comparator.nullsFirst(Comparator.comparing(InstitutionalGroupInstitutionDto::getDepartmentName)).thenComparing(InstitutionalGroupInstitutionDto::getInstitutionName).reversed()).collect(Collectors.toList());
			else
				result = result.stream().sorted(Comparator.nullsFirst(Comparator.comparing(InstitutionalGroupInstitutionDto::getDepartmentName)).thenComparing(InstitutionalGroupInstitutionDto::getInstitutionName)).collect(Collectors.toList());
		}
		return new PageImpl<>(result, pageable, result.size());
	}

	@Override
	public List<InstitutionalGroupInstitutionDto> findAll() {
		return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public List<InstitutionalGroupInstitutionDto> findAllById(List<Integer> ids) {
		return repository.findAllById(ids).stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public Optional<InstitutionalGroupInstitutionDto> findById(Integer id) {
		return repository.findById(id).map(this::mapToDto);
	}

	@Override
	public InstitutionalGroupInstitutionDto save(InstitutionalGroupInstitutionDto entity) {
		InstitutionalGroupInstitution entityToSave = new InstitutionalGroupInstitution();
		entityToSave.setId(entity.getId());
		entityToSave.setInstitutionId(entity.getInstitutionId());
		entityToSave.setInstitutionalGroupId(entity.getInstitutionalGroupId());
		entity.setId(repository.save(entityToSave).getId());
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	@Override
	public Example<InstitutionalGroupInstitutionDto> buildExample(InstitutionalGroupInstitutionDto entity) {
		return Example.of(entity);
	}

	private InstitutionalGroupInstitutionDto mapToDto (InstitutionalGroupInstitution entity){
		InstitutionalGroupInstitutionDto result = new InstitutionalGroupInstitutionDto();
		result.setId(entity.getId());
		result.setInstitutionId(entity.getInstitutionId());
		result.setInstitutionalGroupId(entity.getInstitutionalGroupId());
		return result;
	}

	private InstitutionalGroupInstitutionDto mapToDto (InstitutionalGroupInstitutionVo entity){
		InstitutionalGroupInstitutionDto result = new InstitutionalGroupInstitutionDto();
		result.setId(entity.getId());
		result.setInstitutionId(entity.getInstitutionId());
		result.setInstitutionalGroupId(entity.getInstitutionalGroupId());
		result.setInstitutionName(entity.getInstitutionName());
		result.setDepartmentName(entity.getDepartmentName());
		return result;
	}

}
