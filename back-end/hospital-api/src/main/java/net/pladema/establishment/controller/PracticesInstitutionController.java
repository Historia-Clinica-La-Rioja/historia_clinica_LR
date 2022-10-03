package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.VPracticeInstitutionRepository;
import net.pladema.establishment.repository.entity.VPracticeInstitution;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

@RestController
@RequestMapping("backoffice/practicesinstitution")
public class PracticesInstitutionController extends AbstractBackofficeController<VPracticeInstitution, Integer> {

	public PracticesInstitutionController(VPracticeInstitutionRepository repository) {
		super(new BackofficeRepository<>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<>("description")
		));
	}

}
