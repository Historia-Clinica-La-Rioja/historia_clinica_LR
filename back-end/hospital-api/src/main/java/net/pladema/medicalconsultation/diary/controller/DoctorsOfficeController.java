package net.pladema.medicalconsultation.diary.controller;

import io.swagger.annotations.Api;
import net.pladema.medicalconsultation.appointment.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "DoctorsOffice", tags = { "DoctorsOffice" })
@RequestMapping("/institution/{institutionId}/doctorsoffice")
public class DoctorsOfficeController  {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorsOfficeController.class);

    private DoctorsOfficeRepository doctorsOfficeRepository;

    public DoctorsOfficeController(DoctorsOfficeRepository doctorsOfficeRepository) {
        this.doctorsOfficeRepository = doctorsOfficeRepository;
    }

    @GetMapping
    public ResponseEntity<List<DoctorsOffice>> getAll(@PathVariable(name = "institutionId") Integer institutionId) {
        List<DoctorsOffice> doctorsOffices = doctorsOfficeRepository.getDoctorOfficesByInstitution(institutionId);
        LOG.debug("Get all doctorOffices => {}", doctorsOffices);
        return ResponseEntity.ok(doctorsOffices);
    }

}

