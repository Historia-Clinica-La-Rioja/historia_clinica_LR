package net.pladema.procedure.infrastructure.input.rest;

import net.pladema.procedure.infrastructure.input.rest.validator.permission.BackofficeProcedureTemplateValidator;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateRepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/proceduretemplates")
public class BackofficeProcedureTemplateController extends AbstractBackofficeController<ProcedureTemplate, Integer> {

	public BackofficeProcedureTemplateController(ProcedureTemplateRepository repository,
												 BackofficeProcedureTemplateValidator procedureTemplateValidator) {
		super(repository,procedureTemplateValidator);
	}

}
