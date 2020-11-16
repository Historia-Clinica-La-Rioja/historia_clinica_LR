package net.pladema.establishment.controller;

import net.pladema.establishment.repository.HospitalizationTypeRepository;
import net.pladema.establishment.repository.entity.HospitalizationType;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/hospitalizationtypes")
public class BackofficeHospitalizationTypeController extends AbstractBackofficeController<HospitalizationType, Short>{

    public BackofficeHospitalizationTypeController(HospitalizationTypeRepository repository) {
        super(repository);
    }

}
