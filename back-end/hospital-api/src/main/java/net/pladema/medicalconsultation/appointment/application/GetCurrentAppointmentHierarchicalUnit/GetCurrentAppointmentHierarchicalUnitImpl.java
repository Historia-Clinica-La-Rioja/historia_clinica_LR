package net.pladema.medicalconsultation.appointment.application.GetCurrentAppointmentHierarchicalUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.service.domain.HierarchicalUnitBo;
import net.pladema.medicalconsultation.appointment.application.port.GetHierarchicalUnitStorage;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetCurrentAppointmentHierarchicalUnitImpl implements GetCurrentAppointmentHierarchicalUnit {

	private final GetHierarchicalUnitStorage getHierarchicalUnitStorage;

	@Override
	public HierarchicalUnitBo run(Integer institutionId, Integer patientId) {
		return getHierarchicalUnitStorage.getCurrentAppointmentHierarchicalUnitId(institutionId, patientId);
	}
}
