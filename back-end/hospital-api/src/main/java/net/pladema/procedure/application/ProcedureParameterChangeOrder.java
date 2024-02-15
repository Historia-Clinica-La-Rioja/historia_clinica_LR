package net.pladema.procedure.application;

import lombok.RequiredArgsConstructor;
import net.pladema.procedure.application.port.ProcedureParameterStore;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProcedureParameterChangeOrder {

	private final ProcedureParameterStore store;

	private void updateOrder(Integer id, Function<Short, Short> step) {
		store.findById(id).ifPresent(origin ->
			store.findByProcedureTemplateIdAndOrder(origin.getProcedureTemplateId(), step.apply(origin.getOrderNumber()))
					.ifPresent(dest -> {
						Short tempOrder = origin.getOrderNumber();
						store.updateOrder(origin.getId(), dest.getOrderNumber());
						store.updateOrder(dest.getId(), tempOrder);
					})
		);
	}

	public void increaseOrder(Integer id) {
		this.updateOrder(id, (x) -> (short) (x + 1));
	}

	public void decreaseOrder(Integer id) {
		this.updateOrder(id, (x) -> (short) (x - 1));
	}
}
