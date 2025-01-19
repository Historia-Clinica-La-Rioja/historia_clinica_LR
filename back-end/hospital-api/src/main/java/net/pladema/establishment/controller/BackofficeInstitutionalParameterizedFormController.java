package net.pladema.establishment.controller;


import net.pladema.establishment.repository.InstitutionalParameterizedFormRepository;
import net.pladema.establishment.repository.entity.InstitutionalParameterizedForm;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
@RequestMapping("backoffice/institutionalparameterizedform")
@RestController
public class BackofficeInstitutionalParameterizedFormController extends AbstractBackofficeController<InstitutionalParameterizedForm, Integer> {

	public BackofficeInstitutionalParameterizedFormController(
			InstitutionalParameterizedFormRepository institutionalParameterizedFormRepository
	) {
		super(institutionalParameterizedFormRepository);
	}
}
