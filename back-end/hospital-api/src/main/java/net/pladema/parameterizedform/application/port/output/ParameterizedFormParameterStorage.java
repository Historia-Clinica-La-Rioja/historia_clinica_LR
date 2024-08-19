package net.pladema.parameterizedform.application.port.output;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedFormParameter;

import java.util.List;
import java.util.Optional;

public interface ParameterizedFormParameterStorage {

	Optional<ParameterizedFormParameter> findById(Integer id);

	List<ParameterizedFormParameter> findByFormIdIdAndOrder(Integer id, Short order);

	void updateOrder(List<Integer> id, Short orderNumber);

	List<Integer> findParameterIdsByFormId(Integer formId);

	Optional<Short> getOrderNumberByFormIdAndParameterId(Integer formId, Integer parameterId);

}
