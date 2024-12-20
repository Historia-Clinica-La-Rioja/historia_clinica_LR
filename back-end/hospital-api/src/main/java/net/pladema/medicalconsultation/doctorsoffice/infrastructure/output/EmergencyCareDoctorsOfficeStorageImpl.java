package net.pladema.medicalconsultation.doctorsoffice.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareDoctorsOfficeStorage;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmergencyCareDoctorsOfficeStorageImpl implements EmergencyCareDoctorsOfficeStorage {

	private final DoctorsOfficeRepository doctorsOfficeRepository;

	@Override
	public List<DoctorsOfficeBo> getAllBySectorId(Integer sectorId) {
		return doctorsOfficeRepository.getAllBySectorId(sectorId, EEmergencyCareState.ATENCION.getId());
	}

	@Override
	public Optional<DoctorsOfficeBo> getById(Integer id) {
		return doctorsOfficeRepository.findEmergencyCareDoctorOfficeById(id, EEmergencyCareState.ATENCION.getId());
	}

	@Override
	public List<DoctorsOfficeBo> getAllAvailableBySectorId(Integer sectorId) {
		return doctorsOfficeRepository.getAllBySectorId(sectorId, EEmergencyCareState.ATENCION.getId())
				.stream().filter(DoctorsOfficeBo::isAvailable).collect(Collectors.toList());
	}

}
