package ar.lamansys.refcounterref.application.port;

import java.util.List;
import java.util.Map;

public interface ReferenceAppointmentStorage {

	void save(Integer referenceId, Integer appointmentId, Boolean isProtected);

	Map<Integer, List<Integer>>  getReferenceAppointmentsIdsWithoutCancelledStateId(List<Integer> referenceId);

	Map<Integer, List<Integer>>  getReferenceAppointmentsIds(List<Integer> referenceId);

	boolean referenceHasAppointment(Integer referenceId);

}
