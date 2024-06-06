package net.pladema.establishment.controller;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.RootSectorRepository;
import net.pladema.establishment.repository.entity.RootSector;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BackofficeRootSectorStore implements BackofficeStore<RootSector, Integer> {

	private final RootSectorRepository repository;
	private final BackofficeAuthoritiesValidator authoritiesValidator;

	@Override
	public Page<RootSector> findAll(RootSector example, Pageable pageable) {
		List<RootSector> entitiesByExample = repository.findAll(buildExample(example), pageable.getSort());
		if (!authoritiesValidator.hasRole(ERole.ROOT) && !authoritiesValidator.hasRole(ERole.ADMINISTRADOR)){
			List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(List.of(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
			List<Integer> idsAllowed = repository.getAllIdsByInstitutionsId(allowedInstitutions);
			entitiesByExample = entitiesByExample.stream().filter(rs -> idsAllowed.contains(rs.getId())).collect(Collectors.toList());
		}
		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(entitiesByExample.subList(minIndex, Math.min(maxIndex, entitiesByExample.size())), pageable, entitiesByExample.size());
	}

	@Override
	public List<RootSector> findAll() {
		return repository.findAll();
	}

	@Override
	public List<RootSector> findAllById(List<Integer> ids) {
		return repository.findAllById(ids);
	}

	@Override
	public Optional<RootSector> findById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public RootSector save(RootSector entity) {
		return repository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	@Override
	public Example<RootSector> buildExample(RootSector entity) {
		return Example.of(entity);
	}

}
