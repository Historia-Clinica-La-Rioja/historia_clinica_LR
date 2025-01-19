package net.pladema.procedure.infrastructure.input.rest;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.procedure.application.ProcedureTemplateUpdateStatus;
import net.pladema.procedure.infrastructure.input.rest.validator.entity.BackofficeProcedureTemplateEntityValidator;
import net.pladema.procedure.infrastructure.input.rest.validator.permission.BackofficeProcedureTemplatePermissionValidator;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateRepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/proceduretemplates")
public class BackofficeProcedureTemplateController extends AbstractBackofficeController<ProcedureTemplate, Integer> {

	private ProcedureTemplateUpdateStatus procedureTemplateUpdateStatus;
	private ProcedureTemplateRepository repository;

	public BackofficeProcedureTemplateController(
		ProcedureTemplateRepository repository,
		BackofficeProcedureTemplatePermissionValidator procedureTemplateValidator,
		BackofficeProcedureTemplateEntityValidator procedureTemplateEntityValidator,
		ProcedureTemplateUpdateStatus procedureTemplateUpdateStatus
	)
	{
		super(repository, procedureTemplateValidator, procedureTemplateEntityValidator);
		this.procedureTemplateUpdateStatus = procedureTemplateUpdateStatus;
		this.repository = repository;
	}

	@GetMapping(params = {"excludeInactive=true"})
	public @ResponseBody Page<ProcedureTemplate> getList(Pageable pageable, @RequestParam("excludeInactive") Boolean excludeInactive) {
		logger.debug("GET_LIST {}", "excludeInactive = true");
		List<Short> statusIds = ProcedureTemplate.getAllStatuses();
		if (excludeInactive != null && excludeInactive) {
			statusIds = ProcedureTemplate.getStatusesOtherThanInactive();
		}
		return repository.findByStatusIdIn(statusIds, pageable);
	}

	@PutMapping(value = "/{id}/update-status")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@ResponseStatus(HttpStatus.OK)
	public void updateStatus(@PathVariable Integer id) {
		procedureTemplateUpdateStatus.updateStatus(id);
	}

}
