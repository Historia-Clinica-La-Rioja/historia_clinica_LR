package net.pladema.procedure.infrastructure.input.rest.validator.entity;

import lombok.RequiredArgsConstructor;
import net.pladema.procedure.infrastructure.input.rest.BackofficeProcedureParameterStore;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureParameterDto;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@RequiredArgsConstructor
public class BackofficeProcedureTemplateParameterEntityValidator implements BackofficeEntityValidator<ProcedureParameterDto, Integer> {

	private final BackofficeProcedureParameterStore store;

	private BackofficeValidationException allReadyExists = new BackofficeValidationException("El estudio ya tiene asociado un parámetro con este código LOINC");

	@Override
	public void assertCreate(ProcedureParameterDto entity) {
		var parentProcedureTemplate = entity.getProcedureTemplateId();
		var loincId = entity.getLoincId();
		if (store.existsInProcedureTemplateByLoinc(parentProcedureTemplate, loincId)) {
			throw allReadyExists;
		}
	}

	@Override
	public void assertUpdate(Integer id, ProcedureParameterDto entity) {
		var parentProcedureTemplate = entity.getProcedureTemplateId();
		var loincId = entity.getLoincId();
		if (store.existsInProcedureTemplateByLoinc(parentProcedureTemplate, loincId, id)) {
			throw allReadyExists;
		}
	}

	@Override
	public void assertDelete(Integer id) {

	}
}
