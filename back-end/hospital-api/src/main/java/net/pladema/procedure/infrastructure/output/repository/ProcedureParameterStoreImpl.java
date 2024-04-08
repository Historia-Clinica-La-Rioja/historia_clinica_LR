package net.pladema.procedure.infrastructure.output.repository;

import lombok.RequiredArgsConstructor;
import net.pladema.procedure.application.port.ProcedureParameterStore;

import net.pladema.procedure.domain.ProcedureParameterBo;

import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameter;

import net.pladema.procedure.infrastructure.output.repository.mapper.ProcedureParameterMapper;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProcedureParameterStoreImpl implements ProcedureParameterStore {

	private final ProcedureParameterRepository procedureParameterRepository;
	private final ProcedureParameterMapper procedureParameterMapper;

	@Override
	public Optional<ProcedureParameterBo> findById(Integer id) {
		return procedureParameterRepository.findById(id)
			.map(procedureParameterMapper::toProcedureParameterBo);
	}

	@Override
	public Optional<ProcedureParameterBo> findByProcedureTemplateIdAndOrder(Integer id, Short order) {
		return procedureParameterRepository.findByProcedureTemplateIdAndOrderNumber(id, order)
			.map(procedureParameterMapper::toProcedureParameterBo);
	}

	@Override
	public void updateOrder(Integer id, Short orderNumber) {
		procedureParameterRepository.findById(id).ifPresent(param -> {
			param.setOrderNumber(orderNumber);
			procedureParameterRepository.save(param);
		});
	}
}
