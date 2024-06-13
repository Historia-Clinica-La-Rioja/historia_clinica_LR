package net.pladema.medicalconsultation.appointment.infrastructure.output.repository.appointment;

import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.RecurringAppointmentTypeStorage;

import net.pladema.medicalconsultation.appointment.service.domain.RecurringTypeBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RecurringAppointmentTypeStorageImpl implements RecurringAppointmentTypeStorage {

	public static final String OUTPUT = "Output -> {}";

	@Override
	public List<RecurringTypeBo> getRecurringAppointmentType() {
		List<RecurringTypeBo> result = RecurringAppointmentType.getAllValues();
		log.debug(OUTPUT, result);
		return result;
	}
}
