package net.pladema.establishment.controller;

import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;

import net.pladema.sgx.backoffice.rest.dto.BackofficeDeleteResponse;
import net.pladema.snowstorm.repository.VSnomedGroupConceptRepository;
import net.pladema.snowstorm.repository.entity.VSnomedGroupConcept;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("backoffice/snomedgroupconcepts")
public class BackofficeSnomedGroupConceptsController extends AbstractBackofficeController<VSnomedGroupConcept, Integer>{

	private final BackofficeSnomedRelatedGroupController backofficeSnomedRelatedGroupController;

    public BackofficeSnomedGroupConceptsController(VSnomedGroupConceptRepository repository,
												   BackofficeSnomedRelatedGroupController backofficeSnomedRelatedGroupController) {
        super(new BackofficeRepository<>(
						repository,
						new BackofficeQueryAdapter<VSnomedGroupConcept>() {
							@Override
							public Example<VSnomedGroupConcept> buildExample(VSnomedGroupConcept entity) {
								ExampleMatcher matcher = ExampleMatcher
										.matching()
										.withMatcher("conceptPt", x -> x.ignoreCase().contains())
										;
								return Example.of(entity, matcher);
							}
						}
				)
		);
		this.backofficeSnomedRelatedGroupController = backofficeSnomedRelatedGroupController;
	}

	@Override
	public BackofficeDeleteResponse<Integer> delete(@PathVariable("id") Integer id) {
		return backofficeSnomedRelatedGroupController.delete(id);
	}

	@Override
	public BackofficeDeleteResponse<List<Integer>> deleteMany(@RequestParam List<Integer> ids) {
		return backofficeSnomedRelatedGroupController.deleteMany(ids);
	}
}
