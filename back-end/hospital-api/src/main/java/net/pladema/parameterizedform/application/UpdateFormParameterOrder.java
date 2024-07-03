package net.pladema.parameterizedform.application;

import lombok.RequiredArgsConstructor;

import net.pladema.parameterizedform.application.port.output.ParameterizedFormParameterStorage;
import net.pladema.parameterizedform.application.port.output.ParameterizedFormStorage;
import net.pladema.parameterizedform.domain.enums.EFormStatus;

import org.springframework.stereotype.Service;

import java.util.function.Function;

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

					if (isFormUpdateable)
						parameterizedFormParameterStorage.findByFormIdIdAndOrder(parameterizedFormParameter.getParameterizedFormId(), step.apply(parameterizedFormParameter.getOrderNumber())).ifPresent(dest -> {
							Short tempOrder = parameterizedFormParameter.getOrderNumber();
							parameterizedFormParameterStorage.updateOrder(parameterizedFormParameter.getId(), dest.getOrderNumber());
							parameterizedFormParameterStorage.updateOrder(dest.getId(), tempOrder);
						});

				});
	}

}
