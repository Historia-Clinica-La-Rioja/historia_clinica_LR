package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeCareLineValidator;
import net.pladema.establishment.repository.CareLineRepository;
import net.pladema.establishment.repository.entity.CareLine;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/carelines")
public class BackofficeCareLineController extends AbstractBackofficeController<CareLine, Integer> {

    public BackofficeCareLineController(CareLineRepository repository, BackofficeCareLineValidator backofficeCareLineValidator) {
        super(repository, backofficeCareLineValidator);
    }

}
