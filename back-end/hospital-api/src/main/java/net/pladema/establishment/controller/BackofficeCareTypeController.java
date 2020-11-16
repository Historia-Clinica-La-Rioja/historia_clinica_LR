package net.pladema.establishment.controller;

import net.pladema.establishment.repository.CareTypeRepository;
import net.pladema.establishment.repository.entity.CareType;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/caretypes")
public class BackofficeCareTypeController extends AbstractBackofficeController<CareType, Short>{

    public BackofficeCareTypeController(CareTypeRepository repository) {
        super(repository);
    }

}
