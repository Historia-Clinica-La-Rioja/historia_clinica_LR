package net.pladema.establishment.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.address.repository.entity.Address;
import net.pladema.address.service.AddressService;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/institutions")
public class BackofficeInstitutionController extends AbstractBackofficeController<Institution, Integer> {

	AddressService addressService;
	
	public BackofficeInstitutionController(InstitutionRepository repository, AddressService addressService) {
		super(repository);
		this.addressService = addressService;
	}
	
	@Override
	@PostMapping
	public @ResponseBody
	Institution create(@Valid @RequestBody Institution entity) {
		logger.debug("CREATE {}", entity);
		Address standinAddr = addressService.addAddress(Address.buildDummy());
		entity.setAddressId(standinAddr.getId());
		return super.create(entity);
	}

}
