package net.pladema.establishment.controller;

import net.pladema.establishment.repository.SectorTypeRepository;
import net.pladema.establishment.repository.entity.SectorType;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/sectortypes")
public class BackofficeSectorTypeController extends AbstractBackofficeController<SectorType, Short>{

    public BackofficeSectorTypeController(SectorTypeRepository repository) {
        super(repository);
    }

}
