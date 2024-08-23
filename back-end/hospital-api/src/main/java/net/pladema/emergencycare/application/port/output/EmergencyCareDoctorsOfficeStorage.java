package net.pladema.emergencycare.application.port.output;

import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import java.util.List;

public interface EmergencyCareDoctorsOfficeStorage {

	List<DoctorsOfficeBo> getAllBySectorId(Integer sectorId);
}
