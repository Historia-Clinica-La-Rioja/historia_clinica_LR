package net.pladema.establishment.controller;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.HierarchicalUnitRelationshipRepository;
import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnit;
import net.pladema.establishment.repository.entity.HierarchicalUnitRelationship;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BackofficeHierarchicalUnitStore implements BackofficeStore<HierarchicalUnit, Integer> {

	private final HierarchicalUnitRepository repository;

	private final HierarchicalUnitRelationshipRepository hierarchicalUnitRelationshipRepository;

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private static final Integer SERVICE = 8;

	@Override
	public Page<HierarchicalUnit> findAll(HierarchicalUnit example, Pageable pageable) {
		List<HierarchicalUnit> entitiesByExample = null;
		if(example.getId() != null){
			entitiesByExample = getHierarchicalUnitParents(example.getId());
		} else {
			entitiesByExample = repository.findAll(Example.of(example), pageable.getSort());
		}
		if (!authoritiesValidator.hasRole(ERole.ROOT) && !authoritiesValidator.hasRole(ERole.ADMINISTRADOR)){
			List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(List.of(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
			List<Integer> idsAllowed = repository.getAllIdsByInstitutionsId(allowedInstitutions);
			entitiesByExample = entitiesByExample.stream().filter(hu -> idsAllowed.contains(hu.getId())).collect(Collectors.toList());
		}
		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(entitiesByExample.subList(minIndex, Math.min(maxIndex, entitiesByExample.size())), pageable, entitiesByExample.size());
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
			if(entitySaved.getTypeId().equals(SERVICE)) {
				entitySaved.setClosestServiceId(entitySaved.getId());
				return repository.save(entitySaved);
			}
			return entitySaved;

		}

		entity = repository.save(entity);

		if(entity.getTypeId().equals(SERVICE)) {
			entity.setClosestServiceId(entity.getId());
			return repository.save(entity);
		}

		return entity;
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

	private List<HierarchicalUnit> getHierarchicalUnitParents(Integer hierarchicalUnitId){
		return hierarchicalUnitRelationshipRepository.findParentsIdsByHierarchicalUnitChildId(hierarchicalUnitId);
	}

}