package net.pladema.establishment.controller;

import net.pladema.establishment.repository.OrchestratorRepository;
import net.pladema.establishment.repository.entity.Orchestrator;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("backoffice/orchestrator")
public class BackofficeOrchestratorController extends AbstractBackofficeController<Orchestrator, Integer> {

	private final OrchestratorRepository repository;

	public BackofficeOrchestratorController(OrchestratorRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public Orchestrator create(@Valid @RequestBody Orchestrator entity) {
		return super.create(entity);

	}
}

