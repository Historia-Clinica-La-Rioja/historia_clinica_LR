package net.pladema.establishment.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnomedGroupValidator;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroup;

@RestController
@RequestMapping("backoffice/snomedgroups")
public class BackofficeSnomedGroupController extends AbstractBackofficeController<SnomedGroup, Integer>{

	private final DateTimeProvider dateTimeProvider;

	private final SnomedGroupRepository snomedGroupRepository;

    public BackofficeSnomedGroupController(SnomedGroupRepository repository,
										   DateTimeProvider dateTimeProvider,
										   SnomedGroupRepository snomedGroupRepository,
										   BackofficeSnomedGroupValidator backofficeSnomedGroupValidator) {
        super(new BackofficeRepository<>(
				repository,
				new BackofficeQueryAdapter<SnomedGroup>() {
					@Override
					public Example<SnomedGroup> buildExample(SnomedGroup entity) {
						ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withMatcher("description", x -> x.ignoreCase().contains())
								;
						return Example.of(entity, matcher);
					}
				}
		), backofficeSnomedGroupValidator);
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

}
