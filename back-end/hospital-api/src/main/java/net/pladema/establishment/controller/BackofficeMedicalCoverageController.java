package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeMedicalCoverageValidator;
import net.pladema.patient.controller.dto.BackofficeCoverageDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/medicalcoverages")
public class BackofficeMedicalCoverageController extends AbstractBackofficeController<BackofficeCoverageDto, Integer> {

    public BackofficeMedicalCoverageController(
            BackofficeMedicalCoverageStore backofficeMedicalCoverageStore,
            BackofficeMedicalCoverageValidator medicalCoverageValidator
    ) {
        super(backofficeMedicalCoverageStore, medicalCoverageValidator);
    }


}