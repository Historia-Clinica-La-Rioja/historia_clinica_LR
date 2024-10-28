package net.pladema.establishment.controller;

import net.pladema.address.repository.entity.Address;
import net.pladema.address.service.AddressService;
import net.pladema.establishment.application.backofficedeleteinstitution.BackofficeDeleteInstitution;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeInstitutionValidator;
import net.pladema.establishment.controller.exceptions.BackofficeInstitutionEnumException;
import net.pladema.establishment.controller.exceptions.BackofficeInstitutionException;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.medicine.application.AssociateAllMedicineGroupsToInstitution;
import net.pladema.medicine.application.AssociateAllMedicinesToInstitution;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.backoffice.rest.dto.BackofficeDeleteResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backoffice/institutions")
public class BackofficeInstitutionController extends AbstractBackofficeController<Institution, Integer> {

	AddressService addressService;
	private final AssociateAllMedicineGroupsToInstitution associateAllMedicineGroupsToInstitution;
	private final AssociateAllMedicinesToInstitution associateAllMedicinesToInstitution;
	private final BackofficeDeleteInstitution backofficeDeleteInstitution;

	public BackofficeInstitutionController(InstitutionRepository repository,
										   BackofficeInstitutionValidator backofficeInstitutionValidator,
										   AddressService addressService,
										   BackofficeDeleteInstitution backofficeDeleteInstitution,
										   AssociateAllMedicineGroupsToInstitution associateAllMedicineGroupsToInstitution,
										   AssociateAllMedicinesToInstitution associateAllMedicinesToInstitution) {
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
		this.backofficeDeleteInstitution = backofficeDeleteInstitution;
		this.associateAllMedicineGroupsToInstitution = associateAllMedicineGroupsToInstitution;
		this.associateAllMedicinesToInstitution = associateAllMedicinesToInstitution;
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
		Institution result = super.create(entity);
		associateAllMedicineGroupsToInstitution.run(entity.getId());
		associateAllMedicinesToInstitution.run(entity.getId());
		return result;
	}

	@Override
	@DeleteMapping("/{id}")
	public @ResponseBody
	BackofficeDeleteResponse<Integer> delete(@PathVariable("id") Integer id) {
		logger.debug("DELETE {}", id);
		permissionValidator.assertDelete(id);
		entityValidator.assertDelete(id);
		try {
			backofficeDeleteInstitution.run(id);
		} catch (DataIntegrityViolationException e) {
			throw new BackofficeInstitutionException(
					BackofficeInstitutionEnumException.CONSTRAIN_VIOLATION,
					"No es posible eliminar esta Institución, ya que tiene otras dependencias asociadas"
			);
		}
		return new BackofficeDeleteResponse<>(id);
	}

	@Override
	@GetMapping(params = "!ids")
	public @ResponseBody Page<Institution> getList(Pageable pageable, Institution entity) {
		logger.debug("GET_LIST {}", entity);
		ItemsAllowed<Integer> itemsAllowed = permissionValidator.itemsAllowedToList(entity);
		if (itemsAllowed.all)
			return store.findAll(entity, pageable);

		List<Institution> list = store.findAll(entity, PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()))
				.getContent()
				.stream()
				.filter(institution -> itemsAllowed.ids.contains(institution.getId()))
				.collect(Collectors.toList());

		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(list.subList(minIndex, Math.min(maxIndex, list.size())), pageable, list.size());
	}

}
