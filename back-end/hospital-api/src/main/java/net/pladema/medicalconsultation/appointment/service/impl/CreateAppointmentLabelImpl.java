package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentLabel;

import net.pladema.medicalconsultation.appointment.service.ports.AppointmentLabelStorage;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CreateAppointmentLabelImpl implements CreateAppointmentLabel {

	private static final String OUTPUT = "Output -> {}";

	private final AppointmentLabelStorage appointmentLabelStorage;

	@Override
	public boolean execute(Integer diaryLabelId, Integer appointmentId) {
		log.debug("Input parameters -> diaryLabelId {}, appointmentId {}", diaryLabelId, appointmentId);
		Boolean result = appointmentLabelStorage.updateLabel(diaryLabelId, appointmentId);
		log.debug(OUTPUT, result);
		return result;
	}
}
