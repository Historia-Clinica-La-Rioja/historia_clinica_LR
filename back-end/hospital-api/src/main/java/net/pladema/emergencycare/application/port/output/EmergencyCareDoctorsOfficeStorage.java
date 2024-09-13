package net.pladema.emergencycare.application.port.output;

import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import java.util.List;
import java.util.Optional;

public interface EmergencyCareDoctorsOfficeStorage {

	List<DoctorsOfficeBo> getAllBySectorId(Integer sectorId);
	Optional<DoctorsOfficeBo> getById(Integer id);
}
