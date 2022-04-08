package net.pladema.establishment.controller;

import net.pladema.address.repository.entity.Address;
import net.pladema.address.service.AddressService;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeInstitutionValidator;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("backoffice/institutions")
public class BackofficeInstitutionController extends AbstractBackofficeController<Institution, Integer> {

	AddressService addressService;
	
	public BackofficeInstitutionController(InstitutionRepository repository,
										   BackofficeInstitutionValidator backofficeInstitutionValidator,
										   AddressService addressService) {
		super(
				new BackofficeRepository<>(
						repository,
						new BackofficeQueryAdapter<>() {
							@Override
							public Example<Institution> buildExample(Institution entity) {
								ExampleMatcher matcher = ExampleMatcher
										.matching()
										.withMatcher("name", x -> x.ignoreCase().contains())
										.withMatcher("sisaCode", x -> x.ignoreCase().contains());
								return Example.of(entity, matcher);
							}
						}
				),
						backofficeInstitutionValidator);
		this.addressService = addressService;
	}
	
	@Override
	@PostMapping
	@Transactional
	public @ResponseBody
	Institution create(@Valid @RequestBody Institution entity) {
		logger.debug("CREATE {}", entity);
		permissionValidator.assertCreate(entity);
		Address standinAddr = addressService.addAddress(Address.buildDummy());
		entity.setAddressId(standinAddr.getId());
		return super.create(entity);
	}

}
