package net.pladema.procedure.application;

import lombok.RequiredArgsConstructor;

import net.pladema.procedure.application.port.ProcedureTemplateStore;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProcedureTemplateUpdateStatus {

	private final ProcedureTemplateStore store;

	@Transactional
	public void updateStatus(Integer procedureTemplateId) {
		store
		.findParameterStatus(procedureTemplateId)
		.ifPresent(status -> {
			store.updateStatus(procedureTemplateId, status.getNextState());
		});
	}
}
