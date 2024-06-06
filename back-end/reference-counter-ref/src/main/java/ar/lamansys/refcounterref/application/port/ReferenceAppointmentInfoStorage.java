package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo;

import java.util.Optional;

public interface ReferenceAppointmentInfoStorage {

 	Optional<ReferenceAppointmentBo> getAppointmentData(Integer referenceId);

}
