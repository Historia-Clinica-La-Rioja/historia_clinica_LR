package net.pladema.parameterizedform.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.parameterizedform.application.port.output.ParameterizedFormParameterStorage;
import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormParameterRepository;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedFormParameter;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParameterizedFormParameterStorageImpl implements ParameterizedFormParameterStorage {

	private final ParameterizedFormParameterRepository parameterizedFormParameterRepository;

	private static final String OUTPUT = "Output -> result {}";

	@Override
	public Optional<ParameterizedFormParameter> findById(Integer id) {
		return parameterizedFormParameterRepository.findById(id);
	}

	@Override
	public List<ParameterizedFormParameter> findByFormIdIdAndOrder(Integer formId, Short order) {
		return parameterizedFormParameterRepository.findByParameterizedFormIdAndOrder(formId, order);
	}

	@Override
	public List<Integer> findParameterIdsByFormId(Integer formId){
		log.debug("Input parameters -> parameterizedFormId {}", formId);
		List<Integer> result = parameterizedFormParameterRepository.findAllByParameterizedFormId(formId)
				.stream()
				.map(ParameterizedFormParameter::getParameterId)
				.collect(Collectors.toList());
		log.debug("Output -> result {}", result);
		return result;
	}

	@Override
	public void updateOrder(List<Integer> ids, Short orderNumber) {
		List<ParameterizedFormParameter> parameters = parameterizedFormParameterRepository.findAllById(ids);
		parameters.forEach(parameterizedFormParameter -> {
			parameterizedFormParameter.setOrderNumber(orderNumber);
		});
		parameterizedFormParameterRepository.saveAll(parameters);
	}

	@Override
	public Optional<Short> getOrderNumberByFormIdAndParameterId(Integer formId, Integer parameterId){
		log.debug("Input parameters -> formId {}, parameterId {}", formId, parameterId);
		Optional<Short> result = parameterizedFormParameterRepository.findByParameterizedFormIdAndParameterId(formId, parameterId).map(ParameterizedFormParameter::getOrderNumber);
		log.debug(OUTPUT, result);
		return result;
	}

}
