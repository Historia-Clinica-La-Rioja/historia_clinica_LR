package net.pladema.medicalconsultation.appointment.infraestructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.ExpiredAppointmentStorage;

import net.pladema.medicalconsultation.appointment.infraestructure.output.repository.ExpiredAppointmentRepository;

import net.pladema.medicalconsultation.appointment.infraestructure.output.repository.entity.ExpiredAppointmentReason;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExpiredAppointmentStorageImpl implements ExpiredAppointmentStorage {

	private final ExpiredAppointmentRepository expiredAppointmentRepository;

	@Override
	public void save(Integer appointmentId, Short reasonTypeId, String reason) {
		log.debug("Input parameters -> appointmentId {}, reasonTypeId {}, reason {} ", appointmentId, reasonTypeId, reason);
		expiredAppointmentRepository.save(new ExpiredAppointmentReason(appointmentId, reasonTypeId, reason));
	}

}
