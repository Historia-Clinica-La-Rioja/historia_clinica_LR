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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.pladema.establishment.repository.entity.HierarchicalUnitType.DEPARTAMENTO;
import static net.pladema.establishment.repository.entity.HierarchicalUnitType.DIRECCION;
import static net.pladema.establishment.repository.entity.HierarchicalUnitType.JEFATURA_SALA;
import static net.pladema.establishment.repository.entity.HierarchicalUnitType.SERVICIO;
import static net.pladema.establishment.repository.entity.HierarchicalUnitType.UNIDAD_CONSULTA;
import static net.pladema.establishment.repository.entity.HierarchicalUnitType.UNIDAD_DIAGNOSTICO_TRATAMIENTO;
import static net.pladema.establishment.repository.entity.HierarchicalUnitType.UNIDAD_ENFERMERIA;
import static net.pladema.establishment.repository.entity.HierarchicalUnitType.UNIDAD_INTERNACION;

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
		assertDelete(id);
		repository.deleteById(id);
	}

	private void assertDelete(Integer id) {
		List<Short> hierarchicalUnitDefaultTypes = Arrays.asList(DIRECCION, UNIDAD_DIAGNOSTICO_TRATAMIENTO, UNIDAD_INTERNACION,
				UNIDAD_CONSULTA, UNIDAD_ENFERMERIA, JEFATURA_SALA, DEPARTAMENTO, SERVICIO);
		if (hierarchicalUnitDefaultTypes.contains(id.shortValue()))
			throw new BackofficeValidationException("hierarchical-unit-type.restriction");
		if (repository.typeInUse(id))
			throw new BackofficeValidationException("hierarchical-unit-type.in-use");
	}

	@Override
	public Example<HierarchicalUnitType> buildExample(HierarchicalUnitType entity) {
		return null;
	}

}
