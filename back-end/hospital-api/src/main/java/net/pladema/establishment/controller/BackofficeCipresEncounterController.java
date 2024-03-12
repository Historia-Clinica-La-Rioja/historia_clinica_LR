package net.pladema.establishment.controller;

import net.pladema.cipres.infrastructure.output.repository.CipresEncounter;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounterRepository;
import net.pladema.clinichistory.cipres.application.forwardoutpatientconsultationtocipres.ForwardOutpatientConsultationToCipres;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/cipresencounters")
public class BackofficeCipresEncounterController extends AbstractBackofficeController<CipresEncounter, Integer> {
	private final ForwardOutpatientConsultationToCipres forwardOutpatientConsultationToCipres;

	public BackofficeCipresEncounterController(CipresEncounterRepository repository,
											   ForwardOutpatientConsultationToCipres forwardOutpatientConsultationToCipres) {
		super(new BackofficeRepository<>(
				repository,
				new BackofficeQueryAdapter<>(){
					@Override
					public Example<CipresEncounter> buildExample(CipresEncounter entity) {
						ExampleMatcher customExampleMatcher = ExampleMatcher
								.matching()
								.withMatcher("encounterId", x -> x.ignoreCase().contains())
								.withMatcher("responseCode", ExampleMatcher.GenericPropertyMatcher::startsWith);
						return Example.of(entity, customExampleMatcher);
					}
				}));
		this.forwardOutpatientConsultationToCipres = forwardOutpatientConsultationToCipres;
	}

	@PutMapping(value = "/{id}/forward-outpatient-consultation")
	@PreAuthorize("hasAnyAuthority('ROOT')")
	@ResponseStatus(HttpStatus.OK)
	public void rebuildFile(@PathVariable Long id) {
		forwardOutpatientConsultationToCipres.run(id.intValue());
	}


}
