package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.FetchCustomAppointment;

import net.pladema.medicalconsultation.diary.service.domain.CustomRecurringAppointmentBo;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class FetchCustomAppointmentImpl implements FetchCustomAppointment {

	private final AppointmentService appointmentService;

	private static final String OUTPUT = "Output -> {}";

	@Override
	public CustomRecurringAppointmentBo execute(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		CustomRecurringAppointmentBo result = appointmentService.getCustomAppointment(appointmentId);
		log.debug(OUTPUT, result);
		return result;
	}
}
