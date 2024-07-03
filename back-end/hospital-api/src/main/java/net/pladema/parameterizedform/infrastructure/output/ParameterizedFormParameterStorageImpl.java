package net.pladema.parameterizedform.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.parameterizedform.application.port.output.ParameterizedFormParameterStorage;
import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormParameterRepository;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedFormParameter;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ParameterizedFormParameterStorageImpl implements ParameterizedFormParameterStorage {

	private final ParameterizedFormParameterRepository parameterizedFormParameterRepository;

	@Override
	public Optional<ParameterizedFormParameter> findById(Integer id) {
		return parameterizedFormParameterRepository.findById(id);
	}

	@Override
	public Optional<ParameterizedFormParameter> findByFormIdIdAndOrder(Integer formId, Short order) {
		return parameterizedFormParameterRepository.findByParameterizedFormIdAndOrder(formId, order);
	}

	@Override
	public void updateOrder(Integer id, Short orderNumber) {
		parameterizedFormParameterRepository.findById(id).ifPresent(param -> {
			param.setOrderNumber(orderNumber);
			parameterizedFormParameterRepository.save(param);
		});
	}
}
