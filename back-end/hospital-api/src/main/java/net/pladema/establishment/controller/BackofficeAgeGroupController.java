package net.pladema.establishment.controller;

import net.pladema.establishment.repository.AgeGroupRepository;
import net.pladema.establishment.repository.entity.AgeGroup;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/agegroups")
public class BackofficeAgeGroupController extends AbstractBackofficeController<AgeGroup, Short>{

    public BackofficeAgeGroupController(AgeGroupRepository repository) {
        super(repository);
    }

}
