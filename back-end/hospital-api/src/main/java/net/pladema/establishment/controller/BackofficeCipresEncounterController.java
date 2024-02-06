package net.pladema.establishment.controller;

import net.pladema.cipres.infrastructure.output.repository.CipresEncounter;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounterRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/cipresencounters")
public class BackofficeCipresEncounterController extends AbstractBackofficeController<CipresEncounter, Integer> {

	private final CipresEncounterRepository repository;

	public BackofficeCipresEncounterController(CipresEncounterRepository repository) {
		super(repository);
		this.repository = repository;
	}

}
