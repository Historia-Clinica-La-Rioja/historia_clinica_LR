package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeHierarchicalUnitValidator;
import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnit;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/hierarchicalunits")
public class BackofficeHierarchicalUnitController extends AbstractBackofficeController<HierarchicalUnit, Integer> {

	public BackofficeHierarchicalUnitController(HierarchicalUnitRepository repository,
												BackofficeHierarchicalUnitValidator validator) {
		super(new BackofficeRepository<>(
				repository,
				new BackofficeQueryAdapter<>() {
					@Override
					public Example<HierarchicalUnit> buildExample(HierarchicalUnit entity) {
						ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withMatcher("alias", x -> x.ignoreCase().contains())
								.withMatcher("institutionId", x -> x.ignoreCase().contains());
						return Example.of(entity, matcher);
					}
				}
		), validator);
	}

}