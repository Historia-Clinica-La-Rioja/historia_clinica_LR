package net.pladema.booking.controller;

import net.pladema.booking.repository.entity.VProfessionalMedicalCoverage;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import net.pladema.booking.controller.constraints.BackofficeHealthcareProfessionalHealthInsuranceEntityValidator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/healthcareprofessionalhealthinsurances")
public class BackofficeHealthcareProfessionalHealthInsuranceController
        extends AbstractBackofficeController<VProfessionalMedicalCoverage, Integer> {
    public BackofficeHealthcareProfessionalHealthInsuranceController(BackofficeHealthcareProfessionalHealthInsuranceStore store,
                                                                     BackofficeHealthcareProfessionalHealthInsuranceEntityValidator validator) {
        super(store,
                new BackofficePermissionValidatorAdapter<>(),
                validator);
    }
}
