package net.pladema.medicalconsultation.appointment.service.impl;

import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.ABSENT;
import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.ASSIGNED;
import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.SERVED;
import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.CANCELLED;
import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.CONFIRMED;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.service.AppointmentValidatorService;

@Service
public class AppointmentValidatorServiceImpl implements AppointmentValidatorService {

	public static final String OUTPUT = "Output -> {}";
	private static final Logger LOG = LoggerFactory.getLogger(AppointmentValidatorServiceImpl.class);

	private HashMap<Short, Collection<Short>> validStates;

	private Collection<Short> statesWithReason;

	private final AppointmentRepository appointmentRepository;

	public AppointmentValidatorServiceImpl(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
		validStates = new HashMap<>();
		validStates.put(ASSIGNED, Arrays.asList(CONFIRMED, CANCELLED));
		validStates.put(CONFIRMED, Arrays.asList(ABSENT, CANCELLED));
		validStates.put(ABSENT, Arrays.asList(CONFIRMED));
		validStates.put(SERVED, Arrays.asList(CONFIRMED));
		validStates.put(CANCELLED, Arrays.asList());
		statesWithReason = Arrays.asList(CANCELLED, ABSENT);
	}

	@Override
	public boolean validateStateUpdate(Integer appointmentId, short appointmentStateId, String reason) {
		LOG.debug("Input parameters -> appointmentId {}, appointmentStateId {}, reason {}", appointmentId,
				appointmentStateId, reason);
		Optional<Appointment> apmtOpt = appointmentRepository.findById(appointmentId);
		if (apmtOpt.isPresent() && !validStateTransition(appointmentStateId, apmtOpt.get())) {
			throw new ValidationException("appointment.state.transition.invalid");
		}
		if (!validReason(appointmentStateId, reason)) {
			throw new ValidationException("appointment.state.reason.invalid");
		}
		LOG.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	private boolean validReason(short appointmentStateId, String reason) {
		return !statesWithReason.contains(appointmentStateId) || reason != null;
	}

	private boolean validStateTransition(short appointmentStateId, Appointment apmt) {
		return validStates.get(apmt.getAppointmentStateId()).contains(Short.valueOf(appointmentStateId));
	}

}
