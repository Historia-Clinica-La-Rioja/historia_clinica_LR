package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnomedGroupValidator;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroup;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("backoffice/snomedgroups")
public class BackofficeSnomedGroupController extends AbstractBackofficeController<SnomedGroup, Integer>{

	private final DateTimeProvider dateTimeProvider;

	private final SnomedGroupRepository snomedGroupRepository;

    public BackofficeSnomedGroupController(SnomedGroupRepository repository,
										   DateTimeProvider dateTimeProvider,
										   SnomedGroupRepository snomedGroupRepository,
										   BackofficeSnomedGroupValidator backofficeSnomedGroupValidator) {
        super(repository, backofficeSnomedGroupValidator);
		this.dateTimeProvider = dateTimeProvider;
		this.snomedGroupRepository = snomedGroupRepository;
	}

	@Override
	public SnomedGroup create(@Valid @RequestBody SnomedGroup entity) {
		if (entity.getGroupId() == null) {
			throw new BackofficeValidationException("El grupo de Snomed debe tener otro grupo asociado");
		}
		String ecl = snomedGroupRepository.getById(entity.getGroupId()).getEcl();
		entity.setEcl(ecl);
		entity.setLastUpdate(dateTimeProvider.nowDate());
		return super.create(entity);
	}

	@Override
	public SnomedGroup update(@PathVariable("id") Integer id, @Valid @RequestBody SnomedGroup entity) {
		entity.setLastUpdate(dateTimeProvider.nowDate());
		return super.update(id, entity);
	}
}
