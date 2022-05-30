package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeMedicalCoveragePlanValidator;
import net.pladema.establishment.repository.entity.MedicalCoveragePlan;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/medicalcoverageplans")
public class BackofficeMedicalCoveragePlanController extends AbstractBackofficeController<MedicalCoveragePlan, Integer> {

    public BackofficeMedicalCoveragePlanController(BackofficeMedicalCoveragePlanStore backofficeMedicalCoveragePlanStore, BackofficeMedicalCoveragePlanValidator backofficeMedicalCoveragePlanValidator) {
        super(backofficeMedicalCoveragePlanStore, backofficeMedicalCoveragePlanValidator);
    }

}
