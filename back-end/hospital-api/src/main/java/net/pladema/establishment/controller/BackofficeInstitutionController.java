package net.pladema.establishment.controller;

import net.pladema.address.repository.entity.Address;
import net.pladema.address.service.AddressService;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("backoffice/institutions")
public class BackofficeInstitutionController extends AbstractBackofficeController<Institution, Integer> {

	AddressService addressService;
	
	public BackofficeInstitutionController(InstitutionRepository repository, AddressService addressService) {
		super(
				new BackofficeRepository<Institution, Integer>(
						repository,
						new SingleAttributeBackofficeQueryAdapter<Institution>("name")));
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
