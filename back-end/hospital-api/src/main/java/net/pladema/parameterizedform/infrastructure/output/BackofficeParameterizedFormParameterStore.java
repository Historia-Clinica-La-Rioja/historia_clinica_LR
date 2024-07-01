package net.pladema.parameterizedform.infrastructure.output;

import lombok.RequiredArgsConstructor;

import net.pladema.establishment.repository.ParameterRepository;
import net.pladema.establishment.repository.entity.Parameter;
import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormParameterDto;
import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormParameterRepository;
import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedFormParameter;

import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BackofficeParameterizedFormParameterStore implements BackofficeStore<ParameterizedFormParameterDto, Integer> {

	private final ParameterizedFormParameterRepository parameterizedFormParameterRepository;
	private final ParameterRepository parameterRepository;

	@Override
	public Page<ParameterizedFormParameterDto> findAll(ParameterizedFormParameterDto example, Pageable pageable) {
		Pageable sortedByOrderNumber = PageRequest.of(
				pageable.getPageNumber(),
				pageable.getPageSize(),
				Sort.by("orderNumber").ascending()
		);
		return parameterizedFormParameterRepository.findByParameterizedFormId(example.getParameterizedFormId(), sortedByOrderNumber).map(this::mapEntityToDto);
	}

	@Override
	public List<ParameterizedFormParameterDto> findAll() {
		return parameterizedFormParameterRepository.findAll().stream().sorted(Comparator.comparing(ParameterizedFormParameter::getOrderNumber)).map(this::mapEntityToDto).collect(Collectors.toList());
	}

	@Override
	public List<ParameterizedFormParameterDto> findAllById(List<Integer> ids) {
		return parameterizedFormParameterRepository.findAllById(ids).stream().map(this::mapEntityToDto).collect(Collectors.toList());
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

		return mapEntityToDto(parameterizedFormParameterRepository.save(mapDtoToEntity(entity)));
	}

	@Override
	public void deleteById(Integer id) {
		Integer formId = parameterizedFormParameterRepository.findById(id).get().getParameterizedFormId();
		parameterizedFormParameterRepository.deleteById(id);
		updateParametersOrderNumber(formId);
	}

	@Override
	public Example<ParameterizedFormParameterDto> buildExample(ParameterizedFormParameterDto entity) {
		return Example.of(entity);
	}

	private void updateParametersOrderNumber(Integer formId) {
		List<ParameterizedFormParameter> formParameters = parameterizedFormParameterRepository.findAllByParameterizedFormId(formId)
				.stream()
				.sorted(Comparator.comparing(ParameterizedFormParameter::getOrderNumber))
				.collect(Collectors.toList());
		for (int i = 0; i < formParameters.size(); i++) {
			formParameters.get(i).setOrderNumber((short) (i + 1));
		}
		parameterizedFormParameterRepository.saveAll(formParameters);
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
		Parameter p = parameterRepository.getById(parameterId);
		return p.getLoincId() != null ? parameterRepository.getParameterUnitsOfMeasureByLoincId(p.getLoincId()) : parameterRepository.getParameterUnitsOfMeasureByDescription(p.getDescription());
	}
}
