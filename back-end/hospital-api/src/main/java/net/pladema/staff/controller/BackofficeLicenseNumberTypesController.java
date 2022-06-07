package net.pladema.staff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.controller.dto.LicenseNumberTypeDto;
import net.pladema.staff.controller.mapper.BackofficeLicenseNumberTypeStore;

@RestController
@RequestMapping("backoffice/licensenumbertypes")
public class BackofficeLicenseNumberTypesController extends AbstractBackofficeController<LicenseNumberTypeDto, Short> {

	public BackofficeLicenseNumberTypesController(BackofficeLicenseNumberTypeStore store) {
		super(store);
	}

}
