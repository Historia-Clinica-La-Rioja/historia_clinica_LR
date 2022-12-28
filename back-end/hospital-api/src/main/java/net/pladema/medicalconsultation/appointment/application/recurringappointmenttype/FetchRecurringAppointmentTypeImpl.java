package net.pladema.medicalconsultation.appointment.application.recurringappointmenttype;

import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.RecurringAppointmentTypeStorage;

import net.pladema.medicalconsultation.appointment.service.domain.RecurringTypeBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FetchRecurringAppointmentTypeImpl implements FetchRecurringAppointmentType {

	public static final String OUTPUT = "Output -> {}";

	private final RecurringAppointmentTypeStorage recurringAppointmentTypeStorage;

	public FetchRecurringAppointmentTypeImpl(RecurringAppointmentTypeStorage recurringAppointmentTypeStorage) {
		this.recurringAppointmentTypeStorage = recurringAppointmentTypeStorage;
	}

	@Override
	public List<RecurringTypeBo> run() {
		List<RecurringTypeBo> result = recurringAppointmentTypeStorage.getRecurringAppointmentType();
		log.debug(OUTPUT, result);
		return result;
	}
}
