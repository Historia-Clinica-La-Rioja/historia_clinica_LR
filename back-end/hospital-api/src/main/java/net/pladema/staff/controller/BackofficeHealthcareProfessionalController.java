package net.pladema.staff.controller;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.controller.dto.BackofficeHealthcareProfessionalCompleteDto;
import net.pladema.staff.controller.mapper.BackofficeHealthcareProfessionalStore;

@RestController
@RequestMapping("backoffice/healthcareprofessionals")
public class BackofficeHealthcareProfessionalController extends AbstractBackofficeController<BackofficeHealthcareProfessionalCompleteDto, Integer> {

	public BackofficeHealthcareProfessionalController(BackofficeHealthcareProfessionalStore backofficeHealthcareProfessionalStore) {
		super(backofficeHealthcareProfessionalStore, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET));
	}

}
