package net.pladema.procedure.infrastructure.input.rest;

import net.pladema.procedure.application.ProcedureParameterChangeOrder;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureParameterDto;
import net.pladema.procedure.infrastructure.input.rest.validator.entity.BackofficeProcedureTemplateEntityValidator;
import net.pladema.procedure.infrastructure.input.rest.validator.entity.BackofficeProcedureTemplateParameterEntityValidator;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/proceduretemplateparameters")
public class BackofficeProcedureTemplateParameterController extends AbstractBackofficeController<ProcedureParameterDto, Integer> {

	private static final String UP = "up";
	private static final String DOWN = "down";
	ProcedureParameterChangeOrder procedureParameterChangeOrder;

	public BackofficeProcedureTemplateParameterController(
		BackofficeProcedureParameterStore store,
		ProcedureParameterChangeOrder procedureParameterChangeOrder,
		BackofficeProcedureTemplateEntityValidator procedureTemplateEntityValidator
	)
	{
		super(
			store,
			new BackofficePermissionValidatorAdapter<>(),
			new BackofficeProcedureTemplateParameterEntityValidator(store, procedureTemplateEntityValidator)
		);
		this.procedureParameterChangeOrder = procedureParameterChangeOrder;
	}

	@PutMapping(value = "/{id}/change-order")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@ResponseStatus(HttpStatus.OK)
	public void changeOrder(@PathVariable Integer id, @RequestParam String direction) {
		if (direction.equals(UP)) {
			procedureParameterChangeOrder.increaseOrder(id);
		}
		if (direction.equals(DOWN))
			procedureParameterChangeOrder.decreaseOrder(id);
	}
}
