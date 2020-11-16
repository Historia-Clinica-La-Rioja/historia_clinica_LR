package net.pladema.establishment.controller;

import net.pladema.establishment.repository.SectorOrganizationRepository;
import net.pladema.establishment.repository.entity.SectorOrganization;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/sectororganizations")
public class BackofficeSectorOrganizationController extends AbstractBackofficeController<SectorOrganization, Short>{

    public BackofficeSectorOrganizationController(SectorOrganizationRepository repository) {
        super(repository);
    }

}
