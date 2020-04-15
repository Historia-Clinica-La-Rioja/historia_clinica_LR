package net.pladema.address.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.address.repository.ProvinceRepository;
import net.pladema.address.repository.entity.Province;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/provinces")
public class BackofficeProvinceController extends AbstractBackofficeController<Province, Short>{

	public BackofficeProvinceController(ProvinceRepository repository) {
		super(repository);
	}

}
