package net.pladema.address.controller;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/addresses")
public class BackofficeAddressController extends AbstractBackofficeController<AddressDto, Integer> {

	public BackofficeAddressController(
			BackofficeAddressStore addressStore
	) {
		super(addressStore);
	}

	@PutMapping("/{id}")
	public @ResponseBody AddressDto update(@PathVariable("id") Integer id, @RequestBody AddressDto body) {
		logger.debug("UPDATE[id={}] {}", id, body);
		permissionValidator.assertUpdate(id, body);
		return store.save(body);
	}

}