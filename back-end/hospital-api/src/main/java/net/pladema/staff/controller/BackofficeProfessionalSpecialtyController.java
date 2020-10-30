package net.pladema.staff.controller;

import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.repository.ProfessionalSpecialtyRepository;
import net.pladema.staff.repository.entity.ProfessionalSpecialty;

@RestController
@RequestMapping("backoffice/professionalspecialties")
public class BackofficeProfessionalSpecialtyController extends AbstractBackofficeController<ProfessionalSpecialty, Integer> {

	public BackofficeProfessionalSpecialtyController(ProfessionalSpecialtyRepository repository) {
		super(new BackofficeRepository<ProfessionalSpecialty, Integer>(
				repository,
				new BackofficeQueryAdapter<ProfessionalSpecialty>() {
					@Override
					public Example<ProfessionalSpecialty> buildExample(ProfessionalSpecialty entity) {
						ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withMatcher("description", x -> x.ignoreCase().contains())
								.withMatcher("sctidCode", x -> x.ignoreCase().contains());
						return Example.of(entity, matcher);
					}
				}
				));
	}

}
