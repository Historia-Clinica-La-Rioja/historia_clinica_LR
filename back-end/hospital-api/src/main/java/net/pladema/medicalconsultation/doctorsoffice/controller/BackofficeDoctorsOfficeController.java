package net.pladema.medicalconsultation.doctorsoffice.controller;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.doctorsoffice.controller.constraints.BackofficeDoctorsOfficeEntityValidator;
import net.pladema.medicalconsultation.doctorsoffice.controller.permissions.BackofficeDoctorsOfficeValidator;
import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalTime;

@RestController
@RequestMapping("backoffice/doctorsoffices")
public class BackofficeDoctorsOfficeController extends AbstractBackofficeController<DoctorsOffice, Integer> {

    public BackofficeDoctorsOfficeController(DoctorsOfficeRepository repository,
											 BackofficeDoctorsOfficeValidator doctorsOfficeValidator,
											 BackofficeDoctorsOfficeEntityValidator doctorsOfficeEntityValidator,
											 DiaryRepository diaryRepository,
											 EmergencyCareEpisodeRepository emergencyCareEpisodeRepository) {
        super(new DoctorsOfficeStore(repository, diaryRepository, emergencyCareEpisodeRepository), doctorsOfficeValidator, doctorsOfficeEntityValidator);
    }


    @Override
    public DoctorsOffice create(@Valid @RequestBody DoctorsOffice entity) {
        entity.setOpeningTime(LocalTime.of(00,00,00));
        entity.setClosingTime(LocalTime.of(23,00,00));
        return super.create(entity);
    }
}
