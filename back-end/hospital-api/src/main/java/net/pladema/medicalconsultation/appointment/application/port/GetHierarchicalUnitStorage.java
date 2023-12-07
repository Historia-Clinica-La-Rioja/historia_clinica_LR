package net.pladema.medicalconsultation.appointment.application.port;

import net.pladema.establishment.service.domain.HierarchicalUnitBo;

public interface GetHierarchicalUnitStorage {

	HierarchicalUnitBo getCurrentAppointmentHierarchicalUnitId(Integer institutionId, Integer patientId);
}
