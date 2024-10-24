package net.pladema.medicalconsultation.appointment.infrastructure;

import lombok.RequiredArgsConstructor;

import net.pladema.medicalconsultation.appointment.application.port.AppointmentPatientIdentityAccreditationStatusPort;

import net.pladema.medicalconsultation.appointment.infrastructure.output.repository.AppointmentPatientIdentityAccreditationStatusRepository;

import net.pladema.medicalconsultation.appointment.infrastructure.output.repository.entity.AppointmentPatientIdentityAccreditationStatus;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AppointmentPatientIdentityAccreditationStatusPortImpl implements AppointmentPatientIdentityAccreditationStatusPort {

	private final AppointmentPatientIdentityAccreditationStatusRepository appointmentPatientIdentityAccreditationStatusRepository;

	@Override
	public void save(Integer appointmentId, short patientIdentityAccreditationStatusId, String patientIdentificationHash) {
		appointmentPatientIdentityAccreditationStatusRepository.save(new AppointmentPatientIdentityAccreditationStatus(appointmentId, patientIdentityAccreditationStatusId, patientIdentificationHash));
	}

	@Override
	public void clearAppointmentPatientPreviousIdentificationHashByAppointmentId(Integer appointmentId) {
		if (appointmentPatientIdentityAccreditationStatusRepository.existsById(appointmentId))
			appointmentPatientIdentityAccreditationStatusRepository.deleteById(appointmentId);
	}

	@Override
	public boolean patientIdentityAlreadyAccredited(Integer appointmentId) {
		Optional<AppointmentPatientIdentityAccreditationStatus> accreditationStatus = appointmentPatientIdentityAccreditationStatusRepository.findById(appointmentId);
		return accreditationStatus.isPresent() && accreditationStatus.get().getPatientIdentificationHash() != null;
	}

}
