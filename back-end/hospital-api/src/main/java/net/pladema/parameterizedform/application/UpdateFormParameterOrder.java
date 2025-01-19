package net.pladema.parameterizedform.application;

import lombok.RequiredArgsConstructor;

import net.pladema.parameterizedform.application.port.output.ParameterizedFormParameterStorage;
import net.pladema.parameterizedform.application.port.output.ParameterizedFormStorage;
import net.pladema.parameterizedform.domain.enums.EFormStatus;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedFormParameter;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UpdateFormParameterOrder {

	private final ParameterizedFormParameterStorage parameterizedFormParameterStorage;
	private final ParameterizedFormStorage parameterizedFormStorage;

	public void run(Integer id, Function<Short, Short> step) {
		parameterizedFormParameterStorage.findById(id)
				.ifPresent(parameterizedFormParameter -> {

					Boolean isFormUpdateable = parameterizedFormStorage
							.findFormStatus(parameterizedFormParameter.getParameterizedFormId())
							.map(status -> status.equals(EFormStatus.DRAFT.getId()))
							.orElse(false);

					if (isFormUpdateable){
						var newOrderNumber = step.apply(parameterizedFormParameter.getOrderNumber());
						var oldOrderNumber = parameterizedFormParameter.getOrderNumber();
						List<Integer> idsRelatedToNewOrderNumber = parameterizedFormParameterStorage.findByFormIdIdAndOrder(parameterizedFormParameter.getParameterizedFormId(), newOrderNumber)
								.stream().map(ParameterizedFormParameter::getId).collect(Collectors.toList());
						List<Integer> idsRelatedToOldOrderNumber = parameterizedFormParameterStorage.findByFormIdIdAndOrder(parameterizedFormParameter.getParameterizedFormId(), oldOrderNumber)
								.stream().map(ParameterizedFormParameter::getId).collect(Collectors.toList());
						parameterizedFormParameterStorage.updateOrder(idsRelatedToNewOrderNumber, oldOrderNumber);
						parameterizedFormParameterStorage.updateOrder(idsRelatedToOldOrderNumber, newOrderNumber);
					}
				});
	}

}
