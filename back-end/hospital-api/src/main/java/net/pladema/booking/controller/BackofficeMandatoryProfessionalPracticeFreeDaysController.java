package net.pladema.booking.controller;

import net.pladema.booking.controller.dto.BackofficeMandatoryProfessionalPracticeFreeDaysDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/mandatoryprofessionalpracticefreedays")
public class BackofficeMandatoryProfessionalPracticeFreeDaysController
        extends AbstractBackofficeController<BackofficeMandatoryProfessionalPracticeFreeDaysDto, Integer> {
    public BackofficeMandatoryProfessionalPracticeFreeDaysController(BackofficeMandatoryProfessionalPracticeFreeDaysStore store) {
        super(store);
    }
}