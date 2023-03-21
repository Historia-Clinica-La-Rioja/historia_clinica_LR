package ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment;

import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceAppointmentStorageImpl implements ReferenceAppointmentStorage {

	private final ReferenceAppointmentRepository referenceAppointmentRepository;

	@Override
	public void save(Integer referenceId, Integer appointmentId) {
		log.debug("Input parameters -> referenceId {}, appointmentId {}", referenceId, appointmentId);
		referenceAppointmentRepository.save(new ReferenceAppointment(referenceId, appointmentId));
	}
}
