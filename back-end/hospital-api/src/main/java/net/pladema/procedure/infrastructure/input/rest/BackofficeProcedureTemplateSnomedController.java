package net.pladema.procedure.infrastructure.input.rest;

import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureTemplateDto;
import net.pladema.procedure.infrastructure.input.rest.validator.permission.BackofficeProcedureTemplateSnomedValidator;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.dto.BackofficeDeleteResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/proceduretemplatesnomeds")
public class BackofficeProcedureTemplateSnomedController extends AbstractBackofficeController<ProcedureTemplateDto, Integer> {

	BackofficeProcedureTemplateStore procedureTemplateStore;

	public BackofficeProcedureTemplateSnomedController(BackofficeProcedureTemplateStore store,
													   BackofficeProcedureTemplateSnomedValidator procedureTemplateSnomedValidator) {
		super(store, procedureTemplateSnomedValidator);
		this.procedureTemplateStore = store;
	}

	@DeleteMapping("/{id}/{snomedId}")
	@Transactional
	public @ResponseBody BackofficeDeleteResponse<Integer> delete(@PathVariable("id") Integer id,
																  @PathVariable("snomedId") Integer snomedId) {
		procedureTemplateStore.deleteByProcedureTemplateIdSctid(id,snomedId);
		return super.delete(id);
	}

}