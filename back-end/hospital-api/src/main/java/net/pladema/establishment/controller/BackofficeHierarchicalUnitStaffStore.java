package net.pladema.establishment.controller;

import lombok.RequiredArgsConstructor;

import net.pladema.establishment.repository.HierarchicalUnitStaffRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitStaff;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BackofficeHierarchicalUnitStaffStore implements BackofficeStore<HierarchicalUnitStaff, Integer> {

	private final HierarchicalUnitStaffRepository repository;

	@Override
	public Page<HierarchicalUnitStaff> findAll(HierarchicalUnitStaff entity, Pageable pageable) {
		List<HierarchicalUnitStaff> content = new ArrayList<>();
		if (entity.getHierarchicalUnitId() != null)
			content = repository.findAllByHierarchicalUnitId(entity.getHierarchicalUnitId());
		if (entity.getUserId() != null)
			content = repository.findAllByUserId(entity.getUserId());
		return new PageImpl<>(content, pageable, content.size());
	}

	@Override
	public List<HierarchicalUnitStaff> findAll() {
		return repository.findAll();
	}

	@Override
	public List<HierarchicalUnitStaff> findAllById(List<Integer> ids) {
		return repository.findAllById(ids);
	}

	@Override
	public Optional<HierarchicalUnitStaff> findById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public HierarchicalUnitStaff save(HierarchicalUnitStaff entity) {
		return repository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	@Override
	public Example<HierarchicalUnitStaff> buildExample(HierarchicalUnitStaff entity) {
		return Example.of(entity);
	}


}
