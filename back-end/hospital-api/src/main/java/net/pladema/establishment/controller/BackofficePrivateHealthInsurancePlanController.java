package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficePrivateHealthInsurancePlanValidator;
import net.pladema.establishment.repository.entity.MedicalCoveragePlan;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/privatehealthinsuranceplans")
public class BackofficePrivateHealthInsurancePlanController extends AbstractBackofficeController<MedicalCoveragePlan, Integer> {

    public BackofficePrivateHealthInsurancePlanController(BackofficePrivateHealthInsurancePlanStore backofficePrivateHealthInsurancePlanStore, BackofficePrivateHealthInsurancePlanValidator backofficePrivateHealthInsurancePlanValidator) {
        super(backofficePrivateHealthInsurancePlanStore, backofficePrivateHealthInsurancePlanValidator);
    }

}
