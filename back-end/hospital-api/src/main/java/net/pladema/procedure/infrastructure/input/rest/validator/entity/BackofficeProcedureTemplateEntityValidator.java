package net.pladema.procedure.infrastructure.input.rest.validator.entity;

import lombok.RequiredArgsConstructor;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateRepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackofficeProcedureTemplateEntityValidator implements BackofficeEntityValidator<ProcedureTemplate, Integer> {

	private final ProcedureTemplateRepository procedureTemplateRepository;

	private BackofficeValidationException notUpdateable = new BackofficeValidationException("Los estudios en estado diferente a borrador no pueden modificarse");


	@Override
	public void assertCreate(ProcedureTemplate entity) {
	}

	@Override
	public void assertUpdate(Integer id, ProcedureTemplate entity) {
		var toUpdate = procedureTemplateRepository.getById(id);
		if (!toUpdate.canUpdate())
			throw notUpdateable;
	}

	public void assertUpdate(Integer id) {
		var toUpdate = procedureTemplateRepository.getById(id);
		if (!toUpdate.canUpdate())
			throw notUpdateable;
	}

	@Override
	public void assertDelete(Integer id) {

	}
}
