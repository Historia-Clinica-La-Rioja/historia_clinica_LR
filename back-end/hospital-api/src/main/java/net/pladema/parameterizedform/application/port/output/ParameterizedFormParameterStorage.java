package net.pladema.parameterizedform.application.port.output;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedFormParameter;

import java.util.Optional;

public interface ParameterizedFormParameterStorage {

	Optional<ParameterizedFormParameter> findById(Integer id);
	Optional<ParameterizedFormParameter> findByFormIdIdAndOrder(Integer id, Short order);
	void updateOrder(Integer id, Short orderNumber);

}
