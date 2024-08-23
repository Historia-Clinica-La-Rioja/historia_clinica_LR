package net.pladema.medicalconsultation.doctorsoffice.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareDoctorsOfficeStorage;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EmergencyCareDoctorsOfficeStorageImpl implements EmergencyCareDoctorsOfficeStorage {

	private final DoctorsOfficeRepository doctorsOfficeRepository;

	@Override
	public List<DoctorsOfficeBo> getAllBySectorId(Integer sectorId) {
		return doctorsOfficeRepository.getAllBySectorId(sectorId, EEmergencyCareState.ATENCION.getId());
	}

}
