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

	private final ReferenceAppointmentRepository referenceAppointmentRepository;

	@Override
	public void save(Integer referenceId, Integer appointmentId) {
		log.debug("Input parameters -> referenceId {}, appointmentId {}", referenceId, appointmentId);
		referenceAppointmentRepository.save(new ReferenceAppointment(referenceId, appointmentId));
	}

	@Override
	public Map<Integer, List<Integer>> getReferenceAppointmentsIds(List<Integer> referenceIds) {
		log.debug("Fetch appointment ids by reference ids");
		List<ReferenceAppointmentBo> referenceAppointmentBos = referenceAppointmentRepository.getAppointmentIdsByReferenceIds(referenceIds);
		return referenceAppointmentBos.stream()
				.collect(Collectors.groupingBy(ReferenceAppointmentBo::getReferenceId,
						Collectors.mapping(ReferenceAppointmentBo::getAppointmentId, Collectors.toList())));
	}


}
