package net.pladema.procedure.application;

import lombok.RequiredArgsConstructor;
import net.pladema.procedure.application.port.ProcedureParameterStore;

import net.pladema.procedure.application.port.ProcedureTemplateStore;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProcedureParameterChangeOrder {

	private final ProcedureParameterStore parameterStore;
	private final ProcedureTemplateStore templateStore;

	private void updateOrder(Integer id, Function<Short, Short> step) {
		parameterStore
		.findById(id)
		.ifPresent(origin -> {

			var templateIsUpdateable = templateStore
			.findParameterStatus(origin.getProcedureTemplateId())
			.map(status -> status.isUpdateable())
			.orElseGet(() -> false);

			if (templateIsUpdateable)
				parameterStore.findByProcedureTemplateIdAndOrder(origin.getProcedureTemplateId(), step.apply(origin.getOrderNumber())).ifPresent(dest -> {
					Short tempOrder = origin.getOrderNumber();
					parameterStore.updateOrder(origin.getId(), dest.getOrderNumber());
					parameterStore.updateOrder(dest.getId(), tempOrder);
				});

		});
	}

	public void increaseOrder(Integer id) {
		this.updateOrder(id, (x) -> (short) (x + 1));
	}

	public void decreaseOrder(Integer id) {
		this.updateOrder(id, (x) -> (short) (x - 1));
	}
}
