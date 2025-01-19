package net.pladema.establishment.controller;

import net.pladema.imagenetwork.derivedstudies.repository.ResultStudiesRepository;
import net.pladema.imagenetwork.derivedstudies.repository.entity.ResultStudies;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/resultstudies")
public class BackofficeResultStudiesController extends AbstractBackofficeController<ResultStudies, Integer> {

	private final ResultStudiesRepository repository;

	public BackofficeResultStudiesController(ResultStudiesRepository repository) {
		super(repository);
		this.repository = repository;
	}

}

