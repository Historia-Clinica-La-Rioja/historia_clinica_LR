package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.MedicalCoverageTypeDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/medicalcoveragetypes")
public class BackofficeMedicalCoverageTypeController extends AbstractBackofficeController<MedicalCoverageTypeDto, Short> {

    public BackofficeMedicalCoverageTypeController(BackofficeMedicalCoverageTypeStore store) {
        super(store);
    }

}
