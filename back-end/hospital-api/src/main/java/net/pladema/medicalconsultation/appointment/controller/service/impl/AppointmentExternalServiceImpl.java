package net.pladema.medicalconsultation.appointment.controller.service.impl;

import net.pladema.medicalconsultation.appointment.controller.service.AppointmentExternalService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AppointmentExternalServiceImpl implements AppointmentExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentExternalServiceImpl.class);
	private static final String OUTPUT = "Output -> {}";

	private final AppointmentService appointmentService;

	public AppointmentExternalServiceImpl(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@Override
	public boolean hasConfirmedAppointment(Integer patientId, Integer healthProfessionalId) {
		LOG.debug("Input parameters -> patientId {}, healthProfessionalId {}", patientId, healthProfessionalId);
		boolean result = appointmentService.hasConfirmedAppointment(patientId, healthProfessionalId);
		LOG.debug(OUTPUT, result);
		return result;
	}
}
