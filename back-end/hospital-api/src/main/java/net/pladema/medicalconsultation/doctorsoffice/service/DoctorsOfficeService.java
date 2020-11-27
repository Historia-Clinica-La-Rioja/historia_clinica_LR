package net.pladema.medicalconsultation.doctorsoffice.service;

import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import java.util.List;

public interface DoctorsOfficeService {

    List<DoctorsOfficeBo> getAllDoctorsOffice(Integer institutionId, Integer sectorId);
}
