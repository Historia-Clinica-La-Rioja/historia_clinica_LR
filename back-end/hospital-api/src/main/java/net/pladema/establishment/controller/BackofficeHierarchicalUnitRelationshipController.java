package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeHierarchicalUnitRelationshipValidator;
import net.pladema.establishment.repository.HierarchicalUnitRelationshipRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitRelationship;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("backoffice/hierarchicalunitrelationships")
public class BackofficeHierarchicalUnitRelationshipController extends AbstractBackofficeController<HierarchicalUnitRelationship, Integer> {

	public BackofficeHierarchicalUnitRelationshipController(HierarchicalUnitRelationshipRepository repository,
															BackofficeHierarchicalUnitRelationshipValidator validator) {
		super(repository, validator);
	}

}
