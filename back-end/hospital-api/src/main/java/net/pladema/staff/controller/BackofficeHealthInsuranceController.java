package net.pladema.staff.controller;

import net.pladema.person.repository.entity.HealthInsurance;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.controller.mapper.BackofficeHealthInsuranceStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/healthinsurances")
public class BackofficeHealthInsuranceController extends AbstractBackofficeController<HealthInsurance, Integer> {

    public BackofficeHealthInsuranceController(BackofficeHealthInsuranceStore store) {
        super(store);
    }
}
