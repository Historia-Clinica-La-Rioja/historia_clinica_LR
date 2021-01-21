package net.pladema.medicalconsultation.doctorsoffice.controller.service;

import net.pladema.medicalconsultation.doctorsoffice.controller.dto.DoctorsOfficeDto;

public interface DoctorsOfficeExternalService {

    DoctorsOfficeDto getDoctorsOfficeById(Integer doctorsOfficeId);

}
