package net.pladema.establishment.controller;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeInstitutionPrescriptionValidator;
import net.pladema.establishment.repository.InstitutionPrescriptionRepository;
import net.pladema.establishment.repository.entity.InstitutionPrescription;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;

@RestController
@RequestMapping("backoffice/institutions-prescription")
public class BackofficeInstitutionPrescriptionController extends AbstractBackofficeController<InstitutionPrescription, Integer> {


	public BackofficeInstitutionPrescriptionController(
			InstitutionPrescriptionRepository repository,
			BackofficeInstitutionPrescriptionValidator backofficeInstitutionValidator
	)
		{
		super(
				new BackofficeRepository<>(
						repository,
						new BackofficeQueryAdapter<>() {
							@Override
							public Example<InstitutionPrescription> buildExample(InstitutionPrescription entity) {
								ExampleMatcher matcher = ExampleMatcher
										.matching()
										.withMatcher("name", x -> x.ignoreCase().contains())
										.withMatcher("sisaCode", x -> x.ignoreCase().contains());
								return Example.of(entity, matcher);
							}
						}
				),
				backofficeInstitutionValidator);
	}
}
