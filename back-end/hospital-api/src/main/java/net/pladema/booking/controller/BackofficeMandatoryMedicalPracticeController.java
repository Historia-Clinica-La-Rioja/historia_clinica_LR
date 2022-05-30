package net.pladema.booking.controller;

import net.pladema.booking.repository.BackofficeMandatoryMedicalPracticeRepository;
import net.pladema.booking.repository.entity.BackofficeMandatoryMedicalPractice;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/mandatorymedicalpractices")
public class BackofficeMandatoryMedicalPracticeController
        extends AbstractBackofficeController<BackofficeMandatoryMedicalPractice, Integer> {
    public BackofficeMandatoryMedicalPracticeController(BackofficeMandatoryMedicalPracticeRepository repository) {
        super(repository);
    }
}
