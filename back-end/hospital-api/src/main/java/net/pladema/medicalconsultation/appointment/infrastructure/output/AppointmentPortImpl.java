package net.pladema.medicalconsultation.appointment.infrastructure.output;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentPort;

import net.pladema.medicalconsultation.appointment.repository.AppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AppointmentPortImpl implements AppointmentPort {

	private final AppointmentRepository appointmentRepository;
	private final AppointmentAssnRepository appointmentAssnRepository;

	@Override
	public void updateAppointmentState(Integer appointmentId, Short appointmentStatusId) {
		appointmentRepository.updateState(appointmentId, appointmentStatusId, UserInfo.getCurrentAuditor(), LocalDateTime.now());
	}

	@Override
	public Integer getAppointmentParentId(Integer appointmentId) {
		return appointmentRepository.fetchAppointmentParentId(appointmentId);
	}

	@Override
	public Optional<AppointmentBo> getAppointmentById(Integer appointmentId) {
		return appointmentRepository.getAppointment(appointmentId).stream().findFirst().map(AppointmentBo::fromAppointmentVo);
	}

	@Override
	public Integer getRecurringAppointmentQuantityByAppointmentParentId(Integer appointmentParentId) {
		return appointmentRepository.recurringAppointmentQuantityByParentId(appointmentParentId);
	}

	@Override
	public Short getAppointmentModalityById(Integer appointmentId) {
		return appointmentRepository.getModalityById(appointmentId);
	}

	@Override
	public void deleteAppointmentById(Integer appointmentId) {
		appointmentRepository.deleteById(appointmentId);
	}

	@Override
	public void updateOpeningHoursId(Integer appointmentId, Integer openingHoursId) {
		appointmentAssnRepository.updateOpeningHoursId(appointmentId, openingHoursId);
	}

}
