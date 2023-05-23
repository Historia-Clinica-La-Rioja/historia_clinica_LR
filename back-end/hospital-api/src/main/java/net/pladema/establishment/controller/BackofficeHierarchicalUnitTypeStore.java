package net.pladema.establishment.controller;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.HierarchicalUnitTypeRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitType;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackofficeHierarchicalUnitTypeStore implements BackofficeStore<HierarchicalUnitType, Integer> {

	private final HierarchicalUnitTypeRepository repository;

	@Override
	public Page<HierarchicalUnitType> findAll(HierarchicalUnitType example, Pageable pageable) {
		List<HierarchicalUnitType> list = repository.findAll();
		return new PageImpl<>(list, pageable, list.size());
	}

	@Override
	public List<HierarchicalUnitType> findAll() {
		return repository.findAll();
	}

	@Override
	public List<HierarchicalUnitType> findAllById(List<Integer> ids) {
		if (ids.isEmpty())
			return null;
		return repository.findAllById(ids);
	}

	@Override
	public Optional<HierarchicalUnitType> findById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public HierarchicalUnitType save(HierarchicalUnitType entity) {
		validateHierarchicalUnitType(entity);
		return repository.save(entity);
	}

	private void validateHierarchicalUnitType(HierarchicalUnitType entity) {
		Optional<HierarchicalUnitType> hierarchicalUnitTypeSaved = repository.findByDescription(entity.getDescription());
		if (hierarchicalUnitTypeSaved.isPresent()) {
			if (entity.getId() != null) {
				if (entity.getId() != hierarchicalUnitTypeSaved.get().getId())
					throw new BackofficeValidationException("hierarchical-unit-type.description.exists");
			} else
				throw new BackofficeValidationException("hierarchical-unit-type.description.exists");
		}
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	@Override
	public Example<HierarchicalUnitType> buildExample(HierarchicalUnitType entity) {
		return null;
	}

}
