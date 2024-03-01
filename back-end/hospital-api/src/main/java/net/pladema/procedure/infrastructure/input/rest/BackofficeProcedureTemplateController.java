package net.pladema.procedure.infrastructure.input.rest;

import net.pladema.procedure.application.ProcedureTemplateUpdateStatus;
import net.pladema.procedure.infrastructure.input.rest.validator.entity.BackofficeProcedureTemplateEntityValidator;
import net.pladema.procedure.infrastructure.input.rest.validator.permission.BackofficeProcedureTemplatePermissionValidator;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateRepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/proceduretemplates")
public class BackofficeProcedureTemplateController extends AbstractBackofficeController<ProcedureTemplate, Integer> {

	ProcedureTemplateUpdateStatus procedureTemplateUpdateStatus;

	public BackofficeProcedureTemplateController(
		ProcedureTemplateRepository repository,
		BackofficeProcedureTemplatePermissionValidator procedureTemplateValidator,
		BackofficeProcedureTemplateEntityValidator procedureTemplateEntityValidator,
		ProcedureTemplateUpdateStatus procedureTemplateUpdateStatus
	)
	{
		super(repository, procedureTemplateValidator, procedureTemplateEntityValidator);
		this.procedureTemplateUpdateStatus = procedureTemplateUpdateStatus;
	}

	@PutMapping(value = "/{id}/update-status")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@ResponseStatus(HttpStatus.OK)
	public void updateStatus(@PathVariable Integer id) {
		procedureTemplateUpdateStatus.updateStatus(id);
	}

}
