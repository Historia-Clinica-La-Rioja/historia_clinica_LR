package ar.lamansys.sgx.shared.actuator.infrastructure.input.rest;


import ar.lamansys.sgx.shared.actuator.domain.PropertyBo;
import ar.lamansys.sgx.shared.actuator.infrastructure.output.repository.SystemProperty;
import ar.lamansys.sgx.shared.actuator.infrastructure.output.repository.SystemPropertyRepository;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
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
public class BackofficePropertyStore implements BackofficeStore<PropertyBo, Integer> {

	private final SystemPropertyRepository systemPropertyRepository;

	private final FeatureFlagsService featureFlagsService;

	public BackofficePropertyStore(SystemPropertyRepository systemPropertyRepository,
								   FeatureFlagsService featureFlagsService1) {
		this.systemPropertyRepository = systemPropertyRepository;
		this.featureFlagsService = featureFlagsService1;
	}

	@Override
	public Page<PropertyBo> findAll(PropertyBo entity, Pageable pageable) {
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_VISUALIZACION_PROPIEDADES_SISTEMA))
			return new PageImpl<>(Collections.emptyList(), pageable, 0);

		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("property", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("nodeId", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("value", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		return systemPropertyRepository
				.findAll(Example.of(mapTo(entity), customExampleMatcher),pageable)
				.map(this::mapTo);
	}

	@Override
	public List<PropertyBo> findAll() {
		return featureFlagsService.isOn(AppFeature.HABILITAR_VISUALIZACION_PROPIEDADES_SISTEMA) ?
				systemPropertyRepository.findAll().stream()
						.map(this::mapTo)
						.collect(Collectors.toList()) :
				Collections.emptyList();
	}

	@Override
	public List<PropertyBo> findAllById(List<Integer> ids) {
		return featureFlagsService.isOn(AppFeature.HABILITAR_VISUALIZACION_PROPIEDADES_SISTEMA) ?
				systemPropertyRepository
						.findAllById(ids)
						.stream()
						.map(this::mapTo)
						.collect(Collectors.toList()) :
				Collections.emptyList();
	}

	@Override
	public Optional<PropertyBo> findById(Integer id) {
		return featureFlagsService.isOn(AppFeature.HABILITAR_VISUALIZACION_PROPIEDADES_SISTEMA) ?
				systemPropertyRepository.findById(id)
						.map(this::mapTo) :
				Optional.empty();
	}

	@Override
	public PropertyBo save(PropertyBo dto) {
		return dto;
	}
	
	@Override
	public void deleteById(Integer id) {
		return;
	}

	@Override
	public Example<PropertyBo> buildExample(PropertyBo entity) {
		return Example.of(entity);
	}

	private SystemProperty mapTo(PropertyBo propertyBo) {
		return new SystemProperty(propertyBo.getId(),
				propertyBo.getProperty(),
				propertyBo.getValue(),
				propertyBo.getDescription(),
				propertyBo.getOrigin(),
				propertyBo.getNodeId(),
				propertyBo.getUpdatedOn());
	}

	private PropertyBo mapTo(SystemProperty systemProperty) {
		return new PropertyBo(systemProperty.getId(),
				systemProperty.getProperty(),
				systemProperty.getValue(),
				systemProperty.getDescription(),
				systemProperty.getOrigin(),
				systemProperty.getNodeId(),
				systemProperty.getUpdatedOn());
	}
}
