package net.pladema.establishment.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/snomedrelatedgroups")
public class BackofficeSnomedRelatedGroupController extends AbstractBackofficeController<SnomedRelatedGroup, Integer>{

    public BackofficeSnomedRelatedGroupController(SnomedRelatedGroupRepository repository) {
        super(repository);
    }

}
