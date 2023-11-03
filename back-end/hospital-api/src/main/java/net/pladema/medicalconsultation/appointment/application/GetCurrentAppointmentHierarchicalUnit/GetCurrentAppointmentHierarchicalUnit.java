package net.pladema.medicalconsultation.appointment.application.GetCurrentAppointmentHierarchicalUnit;

import net.pladema.establishment.service.domain.HierarchicalUnitBo;

public interface GetCurrentAppointmentHierarchicalUnit {

	HierarchicalUnitBo run(Integer institutionId, Integer patientId);
}
