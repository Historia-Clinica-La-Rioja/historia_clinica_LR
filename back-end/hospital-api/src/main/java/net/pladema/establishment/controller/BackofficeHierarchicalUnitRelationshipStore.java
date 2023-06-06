package net.pladema.establishment.controller;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.HierarchicalUnitRelationshipRepository;
import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitRelationship;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackofficeHierarchicalUnitRelationshipStore implements BackofficeStore<HierarchicalUnitRelationship, Integer> {

	private final HierarchicalUnitRelationshipRepository repository;
	private final HierarchicalUnitRepository hierarchicalUnitRepository;

	@Override
	public Page<HierarchicalUnitRelationship> findAll(HierarchicalUnitRelationship example, Pageable pageable) {
		return repository.findAll(Example.of(example), pageable);
	}

	@Override
	public List<HierarchicalUnitRelationship> findAll() {
		return repository.findAll();
	}

	@Override
	public List<HierarchicalUnitRelationship> findAllById(List<Integer> ids) {
		return repository.findAllById(ids);
	}

	@Override
	public Optional<HierarchicalUnitRelationship> findById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public HierarchicalUnitRelationship save(HierarchicalUnitRelationship entity) {
		var parents = repository.findParentsIdsByHierarchicalUnitChildId(entity.getHierarchicalUnitChildId());
		if (parents.isEmpty()){
			hierarchicalUnitRepository.setHierarchicalUnitIdToReport(entity.getHierarchicalUnitChildId(), entity.getHierarchicalUnitParentId());
		}
		return repository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {
		var hierarchicalUnitRelationship = repository.findById(id);
		hierarchicalUnitRelationship.ifPresent(hur -> {
			var childHierarchicalUnit = hierarchicalUnitRepository.findById(hur.getHierarchicalUnitChildId()).orElse(null);
			if (childHierarchicalUnit != null && childHierarchicalUnit.getHierarchicalUnitIdToReport() != null && childHierarchicalUnit.getHierarchicalUnitIdToReport().equals(hur.getHierarchicalUnitParentId())){
				hierarchicalUnitRepository.deleteHierarchicalUnitIdToReport(childHierarchicalUnit.getId());
			}
		});
		repository.deleteById(id);
	}

	@Override
	public Example<HierarchicalUnitRelationship> buildExample(HierarchicalUnitRelationship entity) {
		return Example.of(entity);
	}
	
}
