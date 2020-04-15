package net.pladema.address.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.address.repository.AddressRepository;
import net.pladema.address.repository.entity.Address;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/addresses")
public class BackofficeAddressController extends AbstractBackofficeController<Address, Integer> {

	public BackofficeAddressController(
			AddressRepository repository
	) {
		super(repository);
	}


}