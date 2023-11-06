package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReferenceAppointmentStorage {

	void save(Integer referenceId, Integer appointmentId);

	Map<Integer, List<Integer>>  getReferenceAppointmentsIds(List<Integer> referenceId);

	Optional<ReferenceAppointmentBo> getAppointmentData(Integer referenceId);
}
