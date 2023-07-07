package net.pladema.establishment.controller;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.HierarchicalUnitRelationshipRepository;
import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnit;
import net.pladema.establishment.repository.entity.HierarchicalUnitRelationship;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackofficeHierarchicalUnitStore implements BackofficeStore<HierarchicalUnit, Integer> {

	private final HierarchicalUnitRepository repository;

	private final HierarchicalUnitRelationshipRepository hierarchicalUnitRelationshipRepository;

	@Override
	public Page<HierarchicalUnit> findAll(HierarchicalUnit example, Pageable pageable) {
		return repository.findAll(buildExample(example), pageable);
	}

	@Override
	public List<HierarchicalUnit> findAll() {
		return repository.findAll();
	}

	@Override
	public List<HierarchicalUnit> findAllById(List<Integer> ids) {
		return repository.findAllById(ids);
	}

	@Override
	public Optional<HierarchicalUnit> findById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public HierarchicalUnit save(HierarchicalUnit entity) {
		if (entity.getId() == null && entity.getHierarchicalUnitIdToReport() != null) {
			HierarchicalUnit entitySaved = repository.save(entity);
			hierarchicalUnitRelationshipRepository.save(createHierarchicalUnitRelationshipEntity(entitySaved.getId(), entitySaved.getHierarchicalUnitIdToReport(), entitySaved.getCreatedBy(), entitySaved.getCreatedOn()));
			return entitySaved;
		}
		return repository.save(entity);
	}

	private HierarchicalUnitRelationship createHierarchicalUnitRelationshipEntity(Integer hierarchicalUnitChildId,
																				  Integer hierarchicalUnitParentId,
																				  Integer createdBy,
																				  LocalDateTime createdOn) {
		HierarchicalUnitRelationship relationship = new HierarchicalUnitRelationship();
		relationship.setHierarchicalUnitChildId(hierarchicalUnitChildId);
		relationship.setHierarchicalUnitParentId(hierarchicalUnitParentId);
		relationship.setCreatedBy(createdBy);
		relationship.setCreatedOn(createdOn);
		return relationship;
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
		hierarchicalUnitRelationshipRepository.deleteByHierarchicalUnitId(id);
	}

	@Override
	public Example<HierarchicalUnit> buildExample(HierarchicalUnit entity) {
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("alias", x -> x.ignoreCase().contains())
				.withMatcher("institutionId", x -> x.ignoreCase().contains());
		return Example.of(entity, matcher);
	}

}