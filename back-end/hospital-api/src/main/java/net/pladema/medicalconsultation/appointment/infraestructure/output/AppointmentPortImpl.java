package net.pladema.medicalconsultation.appointment.infraestructure.output;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentPort;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AppointmentPortImpl implements AppointmentPort {

	private final AppointmentRepository appointmentRepository;

	@Override
	public void updateAppointmentState(Integer appointmentId, Short appointmentStatusId) {
		appointmentRepository.updateState(appointmentId, appointmentStatusId, UserInfo.getCurrentAuditor(), LocalDateTime.now());
	}

	@Override
	public Integer getAppointmentParentId(Integer appointmentId) {
		return appointmentRepository.fetchAppointmentParentId(appointmentId);
	}

	@Override
	public Short getAppointmentStateIdByAppointmentId(Integer appointmentId) {
		return appointmentRepository.getAppointmentStateIdByAppointmentId(appointmentId);
	}

}
