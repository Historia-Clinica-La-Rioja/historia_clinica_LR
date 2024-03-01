package net.pladema.procedure.infrastructure.output.repository;

import lombok.RequiredArgsConstructor;
import net.pladema.procedure.application.port.ProcedureTemplateStore;

import net.pladema.procedure.domain.EProcedureTemplateStatusBo;

import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProcedureTemplateStoreImpl implements ProcedureTemplateStore {

	private final ProcedureTemplateRepository repository;

	@Override
	public Optional<EProcedureTemplateStatusBo> findParameterStatus(Integer procedureTemplateId) {
		return repository.findById(procedureTemplateId).map(x -> x.toStatusBo());
	}

	@Override
	public void updateStatus(Integer procedureTemplateId, EProcedureTemplateStatusBo nextState) {
		repository.updateStatus(procedureTemplateId, ProcedureTemplate.getStatusId(nextState));
	}
}
