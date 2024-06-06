package net.pladema.procedure.infrastructure.input.rest.validator.entity;

import lombok.RequiredArgsConstructor;
import net.pladema.procedure.infrastructure.input.rest.BackofficeProcedureParameterStore;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureParameterDto;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@RequiredArgsConstructor
public class BackofficeProcedureTemplateParameterEntityValidator implements BackofficeEntityValidator<ProcedureParameterDto, Integer> {

	private final BackofficeProcedureParameterStore store;
	private final BackofficeProcedureTemplateEntityValidator procedureTemplateEntityValidator;

	private BackofficeValidationException allReadyExists = new BackofficeValidationException("El estudio ya tiene asociado un parámetro con este código LOINC");

	@Override
	public void assertCreate(ProcedureParameterDto entity) {
		Integer procedureTemplateId = entity.getProcedureTemplateId();
		procedureTemplateEntityValidator.assertUpdate(procedureTemplateId);
		var loincId = entity.getLoincId();
		if (store.existsInProcedureTemplateByLoinc(procedureTemplateId, loincId)) {
			throw allReadyExists;
		}
	}

	@Override
	public void assertUpdate(Integer id, ProcedureParameterDto entity) {
		Integer procedureTemplateId = entity.getProcedureTemplateId();
		procedureTemplateEntityValidator.assertUpdate(procedureTemplateId);
		var loincId = entity.getLoincId();
		if (store.existsInProcedureTemplateByLoinc(procedureTemplateId, loincId, id)) {
			throw allReadyExists;
		}
	}

	@Override
	public void assertDelete(Integer id) {
		var entity = store.findById(id);
		if (entity.isPresent())
			procedureTemplateEntityValidator.assertUpdate(entity.get().getProcedureTemplateId());
	}
}
