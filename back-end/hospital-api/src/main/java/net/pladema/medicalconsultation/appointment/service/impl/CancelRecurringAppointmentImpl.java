package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.CancelRecurringAppointment;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.impl.exceptions.RecurringAppointmentException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class CancelRecurringAppointmentImpl implements CancelRecurringAppointment {

	private final AppointmentRepository appointmentRepository;

	private final AppointmentService appointmentService;

	private final static String APPOINTMENT_NOT_FOUND = "No se ha encontrado el turno";

	@Transactional
	@Override
	public boolean execute(Integer appointmentId, Boolean cancelAllAppointments) {
		log.debug("Input parameters -> appointmentId {}, cancelAllAppointments {}", appointmentId, cancelAllAppointments);
		Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RecurringAppointmentException(APPOINTMENT_NOT_FOUND));

		if (appointment.getParentAppointmentId() != null)
			appointmentId = appointment.getParentAppointmentId();

		if (cancelAllAppointments) {
			appointmentRepository.cancelAllRecurringAppointments(appointmentId, UserInfo.getCurrentAuditor());
			appointmentRepository.updateState(appointmentId, AppointmentState.CANCELLED, UserInfo.getCurrentAuditor(), LocalDateTime.now());
		} else {
			appointmentRepository.cancelCurrentAndLaterRecurringAppointments(
					appointmentId,
					UserInfo.getCurrentAuditor(),
					appointment.getCreationable().getCreatedOn()
			);
			appointmentService.checkRemainingChildAppointments(appointmentId);
			if (appointment.getParentAppointmentId() == null)
				appointmentRepository.updateState(appointmentId, AppointmentState.CANCELLED, UserInfo.getCurrentAuditor(), LocalDateTime.now());
		}
		AppointmentBo appointmentBo = appointmentService.getAppointment(appointment.getId()).orElseThrow(() -> new RecurringAppointmentException(APPOINTMENT_NOT_FOUND));
		appointmentService.verifyRecurringAppointmentsOverturn(appointmentBo.getDiaryId());
		return true;
	}

	@Transactional
	@Override
	public boolean execute(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RecurringAppointmentException(APPOINTMENT_NOT_FOUND));

		if (appointment.getParentAppointmentId() != null) {
			appointmentService.deleteParentId(appointmentId);
			appointmentService.deleteCustomAppointment(appointmentId);
			appointmentId = appointment.getParentAppointmentId();
		}

		LocalDateTime createdOn = appointment.getCreationable().getCreatedOn();
		appointmentRepository.cancelLaterRecurringAppointments(appointmentId, UserInfo.getCurrentAuditor(), createdOn);
		appointmentService.checkRemainingChildAppointments(appointmentId);

		AppointmentBo appointmentBo = appointmentService.getAppointment(appointment.getId()).orElseThrow(() -> new RecurringAppointmentException(APPOINTMENT_NOT_FOUND));
		appointmentService.verifyRecurringAppointmentsOverturn(appointmentBo.getDiaryId());

		return true;
	}
}
