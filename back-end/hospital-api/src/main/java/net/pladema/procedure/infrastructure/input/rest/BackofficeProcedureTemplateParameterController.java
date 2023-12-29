package net.pladema.procedure.infrastructure.input.rest;

import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureParameterDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/proceduretemplateparameters")
public class BackofficeProcedureTemplateParameterController extends AbstractBackofficeController<ProcedureParameterDto,Integer> {

	public BackofficeProcedureTemplateParameterController(BackofficeProcedureParameterStore store) {
		super(store);
	}

}
