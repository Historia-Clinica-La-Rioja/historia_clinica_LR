package ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment;

import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;

import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceAppointmentStorageImpl implements ReferenceAppointmentStorage {

	private static final Short APPOINTMENT_CANCELLED_STATE = 4;
	
	private static final Short APPOINTMENT_ABSENT_STATE = 3;

	private final ReferenceAppointmentRepository referenceAppointmentRepository;

	@Override
	public void save(Integer referenceId, Integer appointmentId, Boolean isProtected) {
		log.debug("Input parameters -> referenceId {}, appointmentId {}, isProtected {}", referenceId, appointmentId, isProtected);
		referenceAppointmentRepository.save(new ReferenceAppointment(referenceId, appointmentId, isProtected));
	}

	@Override
	public Map<Integer, List<Integer>> getReferenceAppointmentsIdsWithoutCancelledStateId(List<Integer> referenceIds) {
		log.debug("Fetch appointment ids without cancelled state id by reference ids");
		List<ReferenceAppointmentBo> referenceAppointmentBos = referenceAppointmentRepository.getAppointmentIdsByReferenceIds(referenceIds, APPOINTMENT_CANCELLED_STATE);
		return referenceAppointmentBos.stream()
				.collect(Collectors.groupingBy(ReferenceAppointmentBo::getReferenceId,
						Collectors.mapping(ReferenceAppointmentBo::getAppointmentId, Collectors.toList())));
	}

	@Override
	public Map<Integer, List<Integer>> getReferenceAppointmentsIds(List<Integer> referenceIds) {
		log.debug("Fetch appointment ids by reference ids");
		List<ReferenceAppointmentBo> referenceAppointmentBos = referenceAppointmentRepository.getAppointmentIdsByReferenceIds(referenceIds);
		return referenceAppointmentBos.stream()
				.collect(Collectors.groupingBy(ReferenceAppointmentBo::getReferenceId,
						Collectors.mapping(ReferenceAppointmentBo::getAppointmentId, Collectors.toList())));
	}

	@Override
	public boolean referenceHasAppointment(Integer referenceId) {
		var appointmentStates = List.of(APPOINTMENT_CANCELLED_STATE, APPOINTMENT_ABSENT_STATE);
		return referenceAppointmentRepository.referenceHasAppointment(referenceId, appointmentStates);
	}

}
