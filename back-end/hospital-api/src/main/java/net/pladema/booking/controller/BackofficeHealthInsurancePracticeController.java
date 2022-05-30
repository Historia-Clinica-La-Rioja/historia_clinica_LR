package net.pladema.booking.controller;

import net.pladema.booking.controller.dto.BackofficeHealthInsurancePracticeDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/healthinsurancepractices")
public class BackofficeHealthInsurancePracticeController
        extends AbstractBackofficeController<BackofficeHealthInsurancePracticeDto, Integer> {
    public BackofficeHealthInsurancePracticeController(BackofficeHealthInsurancePracticeStore repository) {
        super(repository);
    }
}
