package net.pladema.booking.controller;

import net.pladema.booking.repository.BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository;
import net.pladema.booking.repository.entity.BackofficeClinicalSpecialtyMandatoryMedicalPractice;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/clinicalspecialtymandatorymedicalpractices")
public class BackofficeClinicalSpecialtyMandatoryMedicalPracticeController
        extends AbstractBackofficeController<BackofficeClinicalSpecialtyMandatoryMedicalPractice, Integer> {
    public BackofficeClinicalSpecialtyMandatoryMedicalPracticeController(BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository repository) {
        super(repository);
    }
}
