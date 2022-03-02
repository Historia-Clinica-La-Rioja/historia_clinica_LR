package net.pladema.establishment.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroup;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/snomedgroups")
public class BackofficeSnomedGroupController extends AbstractBackofficeController<SnomedGroup, Integer>{

    public BackofficeSnomedGroupController(SnomedGroupRepository repository) {
        super(repository);
    }

}
