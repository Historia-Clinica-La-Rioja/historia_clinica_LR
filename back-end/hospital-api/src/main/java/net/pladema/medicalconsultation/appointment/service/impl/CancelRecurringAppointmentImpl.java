package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.infraestructure.output.repository.appointment.RecurringAppointmentType;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.CancelRecurringAppointment;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CancelRecurringAppointmentImpl implements CancelRecurringAppointment {

	private final AppointmentRepository appointmentRepository;

	private final AppointmentService appointmentService;

	@Override
	public boolean execute(Integer appointmentId, Boolean cancelAllAppointments) {
		log.debug("Input parameters -> appointmentId {}, cancelAllAppointments {}", appointmentId, cancelAllAppointments);
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

		if (appointment.isPresent() && appointment.get().getParentAppointmentId() != null)
			appointmentId = appointment.get().getParentAppointmentId();

		if (cancelAllAppointments) {
			appointmentRepository.cancelAllRecurringAppointments(appointmentId, UserInfo.getCurrentAuditor());
			appointmentRepository.updateState(appointmentId, AppointmentState.CANCELLED, UserInfo.getCurrentAuditor());
		} else {
			appointmentRepository.cancelCurrentAndLaterRecurringAppointments(
					appointmentId,
					UserInfo.getCurrentAuditor(),
					appointment.get().getCreationable().getCreatedOn()
			);
			appointmentService.checkChildAppointments(appointmentId);
			if (appointment.get().getParentAppointmentId() == null)
				appointmentRepository.updateState(appointmentId, AppointmentState.CANCELLED, UserInfo.getCurrentAuditor());
		}
		return true;
	}

	@Override
	public boolean execute(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

		if (appointment.isPresent() && appointment.get().getParentAppointmentId() != null) {
			appointmentService.deleteParentId(appointmentId);
			appointmentService.deleteCustomAppointment(appointmentId);
			appointmentId = appointment.get().getParentAppointmentId();
		}

		LocalDateTime createdOn = appointment.get().getCreationable().getCreatedOn();
		appointmentRepository.cancelLaterRecurringAppointments(appointmentId, UserInfo.getCurrentAuditor(), createdOn);
		appointmentService.checkChildAppointments(appointmentId);

		return true;
	}
}
