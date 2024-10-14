package net.pladema.medicine.infrastructure.input.rest;

import net.pladema.medicine.infrastructure.input.rest.constraints.validator.BackofficeMedicineGroupEntityValidator;
import net.pladema.medicine.infrastructure.output.BackofficeInstitutionMedicineGroupStore;
import net.pladema.medicine.infrastructure.output.repository.MedicineGroupRepository;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineGroup;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
@RequestMapping("backoffice/medicinegroups")
@RestController
public class BackofficeMedicineGroupController extends AbstractBackofficeController<MedicineGroup, Integer> {

	private final BackofficeInstitutionMedicineGroupStore institutionMedicineGroupStore;

	public BackofficeMedicineGroupController(MedicineGroupRepository repository,
											 BackofficeMedicineGroupEntityValidator validator,
											 BackofficeInstitutionMedicineGroupStore medicineGroupStore)
	{
		super(new BackofficeRepository<>(repository,
				new BackofficeQueryAdapter<>(){
						@Override
						public Example<MedicineGroup> buildExample(MedicineGroup entity) {
							entity.setIsDomain(Boolean.TRUE);
							entity.setDeleted(false);
							ExampleMatcher customExampleMatcher = ExampleMatcher
									.matching()
									.withMatcher("name", x -> x.ignoreCase().contains());
							return Example.of(entity, customExampleMatcher);
						}
					}),
				new BackofficePermissionValidatorAdapter<>(),
				validator
		);
		this.institutionMedicineGroupStore = medicineGroupStore;
	}

	@Override
	public MedicineGroup create(@Valid @RequestBody MedicineGroup entity){
		logger.debug("CREATE {}", entity);
		entityValidator.assertCreate(entity);
		entity.setIsDomain(Boolean.TRUE);
		if (entity.getRequiresAudit().equals(Boolean.FALSE)) entity.setRequiredDocumentation(null);
		store.save(entity);
		institutionMedicineGroupStore.associateGroupToAllInstitutions(entity.getId());
		return entity;
	}

	@Override
	public MedicineGroup update(@PathVariable("id") Integer id, @RequestBody MedicineGroup body) {
		logger.debug("UPDATE[id={}] {}", id, body);
		permissionValidator.assertUpdate(id, body);
		entityValidator.assertUpdate(id, body);
		if (body.getRequiresAudit().equals(Boolean.FALSE)) body.setRequiredDocumentation(null);
		return store.save(body);
	}

}
