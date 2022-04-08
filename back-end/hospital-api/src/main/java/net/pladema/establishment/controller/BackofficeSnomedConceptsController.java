package net.pladema.establishment.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/snomedconcepts")
public class BackofficeSnomedConceptsController extends AbstractBackofficeController<Snomed, Integer>{

    public BackofficeSnomedConceptsController(SnomedRepository repository) {
        super(repository);
    }

}
