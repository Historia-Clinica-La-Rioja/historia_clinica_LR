package net.pladema.medicalconsultation.appointment.infrastructure.port;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.establishment.service.domain.HierarchicalUnitBo;
import net.pladema.medicalconsultation.appointment.application.port.GetHierarchicalUnitStorage;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;

import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetHierarchicalUnitStorageImpl implements GetHierarchicalUnitStorage {

	private final AppointmentRepository appointmentRepository;
	private final InstitutionExternalService institutionExternalService;
	private final DateTimeProvider dateTimeProvider;
	private static final String OUTPUT = "Output -> {}";


	@Override
	public HierarchicalUnitBo getCurrentAppointmentHierarchicalUnitId(Integer institutionId, Integer patientId) {

		log.debug("Input parameters -> patientId {}, institutionId {}", patientId, institutionId);

		var appointmentId = this.getCurrentAppointmentId(patientId, institutionId);
		if(appointmentId == null)
			return null;

		var hierarchicalUnit = appointmentRepository.findDiaryHierarchicalUnitIdByAppointment(appointmentId).orElse(null);

		if (hierarchicalUnit != null )
			return new HierarchicalUnitBo(hierarchicalUnit.getId(), hierarchicalUnit.getAlias());
		return null;

	}

	private Integer getCurrentAppointmentId(Integer patientId, Integer institutionId) {
		log.debug("Input parameters -> patientId {}, institutionId {}", patientId, institutionId);

		ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
		var currentDateTime = dateTimeProvider.nowDateTimeWithZone(institutionZoneId);
		var userId = UserInfo.getCurrentAuditor();
		var getCurrentAppointmentsByPatient = appointmentRepository.getCurrentAppointmentsByPatient(patientId,
				institutionId, userId, currentDateTime.toLocalDate());
		if(getCurrentAppointmentsByPatient.isEmpty())
			return null;

		var result = getCurrentAppointmentsByPatient.get(0);
		log.debug(OUTPUT, result);
		return result;
	}
}
