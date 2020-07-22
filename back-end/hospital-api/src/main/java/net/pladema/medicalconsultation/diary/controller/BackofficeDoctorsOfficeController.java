package net.pladema.medicalconsultation.diary.controller;

import net.pladema.medicalconsultation.appointment.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.diary.controller.constraints.BackofficeDoctorsOfficeEntityValidator;
import net.pladema.medicalconsultation.diary.controller.permissions.BackofficeDoctorsOfficeValidator;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/doctorsoffices")
public class BackofficeDoctorsOfficeController extends AbstractBackofficeController<DoctorsOffice, Integer> {

    public BackofficeDoctorsOfficeController(DoctorsOfficeRepository repository,
                                             BackofficeDoctorsOfficeValidator doctorsOfficeValidator,
                                             BackofficeDoctorsOfficeEntityValidator doctorsOfficeEntityValidator) {
        super(repository, doctorsOfficeValidator, doctorsOfficeEntityValidator);
    }
}
