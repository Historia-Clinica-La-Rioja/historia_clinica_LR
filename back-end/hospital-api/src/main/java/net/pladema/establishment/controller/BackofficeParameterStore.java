package net.pladema.establishment.controller;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.controller.dto.EParameterType;
import net.pladema.establishment.controller.dto.ParameterDto;
import net.pladema.establishment.repository.ParameterRepository;
import net.pladema.establishment.repository.ParameterTextOptionRepository;
import net.pladema.establishment.repository.ParameterUnitOfMeasureRepository;
import net.pladema.establishment.repository.entity.Parameter;
import net.pladema.establishment.repository.entity.ParameterTextOption;
import net.pladema.establishment.repository.entity.ParameterUnitOfMeasure;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class BackofficeParameterStore implements BackofficeStore<ParameterDto, Integer>  {

	private final ParameterRepository parameterRepository;
	private final ParameterTextOptionRepository parameterTextOptionRepository;
	private final ParameterUnitOfMeasureRepository parameterUnitOfMeasureRepository;

	@Override
	public Page<ParameterDto> findAll(ParameterDto example, Pageable pageable) {
		List<ParameterDto> result = findAll();

		int minIndex = pageable.getPageNumber() * pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(result.subList(minIndex, Math.min(maxIndex, result.size())), pageable, result.isEmpty() ? 0 : result.size());
	}

	@Override
	public List<ParameterDto> findAll() {
		Stream<ParameterDto> resultStream = parameterRepository.findAll()
				.stream()
				.map(this::mapEntityToDto)
				.peek(dto -> dto.setTextOptions(parameterTextOptionRepository.getDescriptionsFromParameterId(dto.getId())));

		return groupParametersAndSetUnitsOfMeasure(resultStream);
	}

	@Override
	public List<ParameterDto> findAllById(List<Integer> ids) {
		 Stream<ParameterDto> resultStream = parameterRepository.findAllById(ids)
				.stream()
				.map(this::mapEntityToDto)
				.peek(dto -> dto.setTextOptions(parameterTextOptionRepository.getDescriptionsFromParameterId(dto.getId())));

		 return groupParametersAndSetUnitsOfMeasure(resultStream);
	}

	@Override
	public Optional<ParameterDto> findById(Integer id) {
		Optional<Parameter> optionalParameter = parameterRepository.findById(id);
		if (optionalParameter.isPresent()) {
			Parameter parameter = optionalParameter.get();
			ParameterDto parameterDto = mapEntityToDto(parameter);
			if (isNumeric(parameterDto.getTypeId())) {
				List<Integer> parametersIds = parameter.getLoincId() != null ? parameterRepository.getParametersIdsByLoincId(parameterDto.getLoincId()) : parameterRepository.getParametersIdsByDescription(parameterDto.getDescription());
				List<Integer> unitsOfMeasureIds = parametersIds.stream().map(parameterUnitOfMeasureRepository::getUnitOfMeasureFromParameterId).collect(Collectors.toList());
				parameterDto.setUnitsOfMeasureIds(unitsOfMeasureIds);
			}
			else if (isOptionList(parameterDto.getTypeId()))
				parameterDto.setTextOptions(parameterTextOptionRepository.getDescriptionsFromParameterId(parameterDto.getId()));
			return Optional.of(parameterDto);
		}
		return Optional.empty();
	}

	@Override
	public ParameterDto save(ParameterDto entity) {
		return (entity.getId() != null) ? this.updateParameter(entity) : this.createParameter(entity);
	}

	@Override
	public void deleteById(Integer id) {}

	@Override
	public Example<ParameterDto> buildExample(ParameterDto entity) {
		return Example.of(entity);
	}

	private ParameterDto createParameter(ParameterDto entity) {
		if (!isNumeric(entity.getTypeId())) {
			Parameter entitySaved = parameterRepository.save(mapDtoToEntity(entity));
			if (isOptionList(entity.getTypeId()))
				saveTextOptions(entitySaved.getId(), entity.getTextOptions());

			return mapEntityToDto(entitySaved);
		}

		Parameter entitySaved = new Parameter();

		if (satisfiesUnitOfMeasureRequirement(entity)) {
			for (Integer unitsOfMeasureId : entity.getUnitsOfMeasureIds()) {
				entitySaved = parameterRepository.save(mapDtoToEntity(entity));
				parameterUnitOfMeasureRepository.save(new ParameterUnitOfMeasure(entitySaved.getId(), unitsOfMeasureId));
			}
		}
		return mapEntityToDto(entitySaved);
	}

	private ParameterDto updateParameter(ParameterDto newParameterDto) {
		Optional<Parameter> opParameter = parameterRepository.findById(newParameterDto.getId());
		if (opParameter.isEmpty())
			return null;

		Parameter oldParameter = opParameter.get();
		updateOldParameter(oldParameter, newParameterDto);

		if (isNumeric(oldParameter.getTypeId()))
			deleteUnitsOfMeasures(oldParameter);

		if (isOptionList(oldParameter.getTypeId()))
			parameterTextOptionRepository.deleteTextOptionFromParameterId(oldParameter.getId());

		if (isOptionList(newParameterDto.getTypeId()))
			saveTextOptions(oldParameter.getId(), newParameterDto.getTextOptions());

		oldParameter.setTypeId(newParameterDto.getTypeId());

		if (!isNumeric(newParameterDto.getTypeId())) {
			parameterRepository.save(oldParameter);
			return mapEntityToDto(oldParameter);
		}

		if (satisfiesUnitOfMeasureRequirement(newParameterDto)) {
			saveParametersAndUnitsOfMeasure(newParameterDto, oldParameter);
			return mapEntityToDto(oldParameter);
		}
		return null;
	}

	private Boolean isNumeric(Short parameterType) {
		return parameterType.equals(EParameterType.NUMERIC.getId());
	}

	private Boolean isOptionList(Short parameterType) {
		return parameterType.equals(EParameterType.OPTIONS_LIST.getId());
	}

	private Boolean satisfiesUnitOfMeasureRequirement(ParameterDto parameterDto) {
		return parameterDto.getUnitsOfMeasureIds().size() >= parameterDto.getInputCount();
	}

	private void updateOldParameter(Parameter oldParameter, ParameterDto newParameterDto) {
		if (newParameterDto.getLoincId() != null) {
			oldParameter.setDescription(null);
			oldParameter.setLoincId(newParameterDto.getLoincId());
		} else {
			oldParameter.setDescription(newParameterDto.getDescription());
			oldParameter.setLoincId(null);
		}
		oldParameter.setInputCount(newParameterDto.getInputCount());
		oldParameter.setSnomedGroupId(newParameterDto.getSnomedGroupId());
	}

	private void deleteUnitsOfMeasures(Parameter oldParameter) {
		List<Integer> parameterIds = oldParameter.getLoincId() != null ? parameterRepository.getParametersIdsByLoincId(oldParameter.getLoincId()) : parameterRepository.getParametersIdsByDescription(oldParameter.getDescription());
		parameterIds.forEach(parameterId -> {
			parameterUnitOfMeasureRepository.deleteUnitOfMeasureFromParameterId(parameterId);
			if (!parameterId.equals(oldParameter.getId())) {
				parameterRepository.deleteById(parameterId);
			}
		});
	}

	private void saveParametersAndUnitsOfMeasure(ParameterDto newParameterDto, Parameter oldParameter) {
		List<Integer> unitsOfMeasureIds = newParameterDto.getUnitsOfMeasureIds();
		Parameter entitySaved = parameterRepository.save(oldParameter);
		parameterUnitOfMeasureRepository.save(new ParameterUnitOfMeasure(entitySaved.getId(), unitsOfMeasureIds.get(0)));

		for (int i = 1; i < unitsOfMeasureIds.size(); i++) {
			Parameter newParameterCopy = new Parameter(oldParameter.getLoincId(), oldParameter.getDescription(), oldParameter.getTypeId(), oldParameter.getInputCount(), oldParameter.getSnomedGroupId());
			entitySaved = parameterRepository.save(newParameterCopy);
			parameterUnitOfMeasureRepository.save(new ParameterUnitOfMeasure(entitySaved.getId(), unitsOfMeasureIds.get(i)));
		}
	}

	private void saveTextOptions(Integer id, List<String> textOptions) {
		for (String textOption : textOptions)
			saveParameterTextOption(id, textOption);
	}

	private void saveParameterTextOption(Integer entityId, String textOption) {
		ParameterTextOption parameterTextOption = new ParameterTextOption();
		parameterTextOption.setDescription(textOption);
		parameterTextOption.setParameterId(entityId);
		parameterTextOptionRepository.save(parameterTextOption);
	}

	private List<ParameterDto> groupParametersAndSetUnitsOfMeasure(Stream<ParameterDto> parameterDtoStream) {
		return  parameterDtoStream.collect(Collectors.groupingBy(dto -> dto.getLoincId() != null ? dto.getLoincId() : dto.getDescription()))
				.values().stream()
				.map(parameterGroup -> {
					ParameterDto firstDto = parameterGroup.get(0);
					List<Integer> allUnitsOfMeasureIds = parameterGroup.stream()
							.map(dto -> parameterUnitOfMeasureRepository.getUnitOfMeasureFromParameterId(dto.getId()))
							.collect(Collectors.toList());
					firstDto.setUnitsOfMeasureIds(allUnitsOfMeasureIds);
					return firstDto;
				})
				.collect(Collectors.toList());
	}

	private Parameter mapDtoToEntity(ParameterDto parameterDto) {
		return new Parameter(parameterDto.getId(),
				parameterDto.getLoincId(),
				parameterDto.getDescription(),
				parameterDto.getTypeId(),
				parameterDto.getInputCount(),
				parameterDto.getSnomedGroupId());
	}

	private ParameterDto mapEntityToDto(Parameter parameter) {
		return new ParameterDto(parameter.getId(),
				parameter.getLoincId(),
				parameter.getDescription(),
				parameter.getTypeId(),
				parameter.getInputCount(),
				parameter.getSnomedGroupId());
	}

}
