package net.pladema.parameterizedform.infrastructure.output;

import lombok.RequiredArgsConstructor;

import ar.lamansys.sgh.shared.domain.forms.enums.EParameterType;
import net.pladema.parameter.infrastructure.output.repository.ParameterRepository;
import net.pladema.parameter.infrastructure.output.repository.ParameterUnitOfMeasureRepository;
import net.pladema.parameter.infrastructure.output.repository.entity.Parameter;
import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormParameterDto;
import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormParameterRepository;
import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedFormParameter;

import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BackofficeParameterizedFormParameterStore implements BackofficeStore<ParameterizedFormParameterDto, Integer> {

	private final ParameterizedFormParameterRepository parameterizedFormParameterRepository;
	private final ParameterRepository parameterRepository;
	private final ParameterUnitOfMeasureRepository parameterUnitOfMeasureRepository;

	@Override
	public Page<ParameterizedFormParameterDto> findAll(ParameterizedFormParameterDto example, Pageable pageable) {

		Map<Short, List<ParameterizedFormParameter>> parametersMap = parameterizedFormParameterRepository.findAllByParameterizedFormId(example.getParameterizedFormId())
				.stream().collect(Collectors.groupingBy(ParameterizedFormParameter::getOrderNumber));

		List<ParameterizedFormParameterDto> resultList = new ArrayList<>();

		for (Short order: parametersMap.keySet())
			resultList.add(getParameterizedFormParameterFromList(parametersMap.get(order)));

		resultList.sort(Comparator.comparing(ParameterizedFormParameterDto::getOrderNumber));

		int minIndex = pageable.getPageNumber() * pageable.getPageSize();
		int maxIndex = Math.min(minIndex + pageable.getPageSize(), resultList.size());

		return new PageImpl<>(resultList.subList(minIndex, maxIndex), pageable, resultList.size());
	}

	private ParameterizedFormParameterDto getParameterizedFormParameterFromList(List<ParameterizedFormParameter> parameters){
		List<ParameterizedFormParameterDto> parameterList = parameters.stream().map(this::mapEntityToDto).collect(Collectors.toList());
		ParameterizedFormParameterDto result = parameterList.get(0);
		if (parameters.size() > 1){
			List<Integer> unitsOfMeasureIds = new ArrayList<>(result.getUnitsOfMeasureIds()); // Create a new mutable list
			for (int i = 1; i < parameters.size(); i++) {
				unitsOfMeasureIds.addAll(parameterList.get(i).getUnitsOfMeasureIds());
			}
			result.setUnitsOfMeasureIds(unitsOfMeasureIds);
		}
		return result;
	}

	@Override
	public List<ParameterizedFormParameterDto> findAll() {
		return parameterizedFormParameterRepository.findAll()
				.stream()
				.sorted(Comparator.comparing(ParameterizedFormParameter::getOrderNumber))
				.map(this::mapEntityToDto).collect(Collectors.toList());
	}

	@Override
	public List<ParameterizedFormParameterDto> findAllById(List<Integer> ids) {
		return parameterizedFormParameterRepository.findAllById(ids)
				.stream()
				.map(this::mapEntityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ParameterizedFormParameterDto> findById(Integer id) {
		return parameterizedFormParameterRepository.findById(id).stream().map(this::mapEntityToDto).findFirst();
	}

	@Override
	public ParameterizedFormParameterDto save(ParameterizedFormParameterDto entity) {
		List<Short> orders = parameterizedFormParameterRepository.getLastFormsDescendingById(entity.getParameterizedFormId());

		if (orders.isEmpty()) entity.setOrderNumber((short) 1);
		else entity.setOrderNumber((short) (orders.get(0) + 1));

		saveRelatedNumericParametersToForm(entity);

		return mapEntityToDto(parameterizedFormParameterRepository.save(mapDtoToEntity(entity)));
	}

	private void saveRelatedNumericParametersToForm(ParameterizedFormParameterDto entity){
		parameterRepository.findById(entity.getParameterId())
				.ifPresent(p -> {
					if (p.getTypeId().equals(EParameterType.NUMERIC.getId())){
						saveRelatedParameters(p, entity.getParameterizedFormId(), entity.getOrderNumber());
					}
				});
	}

	private void saveRelatedParameters(Parameter parameter, Integer formId, Short orderNumber){
		List<Integer> parameterIds = parameter.getLoincId() != null ?
				parameterRepository.getParametersIdsByLoincId(parameter.getLoincId()) : parameterRepository.getParametersIdsByDescription(parameter.getDescription());

		parameterIds.stream()
				.filter(id -> !id.equals(parameter.getId()))
				.forEach(id -> parameterizedFormParameterRepository.save(new ParameterizedFormParameter(formId, id, orderNumber)));
	}

	@Override
	public void deleteById(Integer id) {
		var parameterizedFormParameter = parameterizedFormParameterRepository.findById(id).orElse(null);
		if (parameterizedFormParameter == null) return;
		List<Integer> idsToDelete = parameterizedFormParameterRepository.findByParameterizedFormIdAndOrder(parameterizedFormParameter.getParameterizedFormId(), parameterizedFormParameter.getOrderNumber())
				.stream().map(ParameterizedFormParameter::getId).collect(Collectors.toList());
		parameterizedFormParameterRepository.deleteAllById(idsToDelete);
		updateParametersOrderNumber(parameterizedFormParameter.getParameterizedFormId());
	}

	@Override
	public Example<ParameterizedFormParameterDto> buildExample(ParameterizedFormParameterDto entity) {
		return Example.of(entity);
	}

	private void updateParametersOrderNumber(Integer formId) {
		Map<Short, List<ParameterizedFormParameter>> formParameters = parameterizedFormParameterRepository.findAllByParameterizedFormId(formId)
				.stream()
				.sorted(Comparator.comparing(ParameterizedFormParameter::getOrderNumber))
				.collect(Collectors.groupingBy(ParameterizedFormParameter::getOrderNumber));

		List<ParameterizedFormParameter> updatedParameters = new ArrayList<>();

		short newOrderNumber = 1;
		for (Short key: formParameters.keySet()){
			short modifiedOrderNumber = newOrderNumber;
			formParameters.get(key).forEach(p -> p.setOrderNumber(modifiedOrderNumber));
			newOrderNumber++;
			updatedParameters.addAll(formParameters.get(key));
		}
		parameterizedFormParameterRepository.saveAll(updatedParameters);
	}

	private ParameterizedFormParameter mapDtoToEntity(ParameterizedFormParameterDto dto) {
		return new ParameterizedFormParameter(dto.getId(), dto.getParameterizedFormId(), dto.getParameterId(), dto.getOrderNumber());
	}

	private ParameterizedFormParameterDto mapEntityToDto(ParameterizedFormParameter entity) {
		ParameterizedFormParameterDto dto = new ParameterizedFormParameterDto(
				entity.getId(),
				entity.getParameterizedFormId(),
				entity.getParameterId(),
				entity.getOrderNumber()
		);

		var isANumericParameter = parameterRepository.isANumericParameter(entity.getParameterId());
		if (isANumericParameter)
			dto.setUnitsOfMeasureIds(getParametersUnitsOfMeasures(entity.getParameterId()));
		return dto;
	}

	private List<Integer> getParametersUnitsOfMeasures(Integer parameterId) {
		return List.of(Objects.requireNonNull(parameterUnitOfMeasureRepository.getUnitOfMeasureFromParameterId(parameterId)));
	}

}
