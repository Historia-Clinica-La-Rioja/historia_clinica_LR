package net.pladema.address.controller;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/addresses")
public class BackofficeAddressController extends AbstractBackofficeController<AddressDto, Integer> {

	public BackofficeAddressController(
			BackofficeAddressStore addressStore
	) {
		super(addressStore);
	}


}