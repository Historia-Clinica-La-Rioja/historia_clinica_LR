package ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment;

import ar.lamansys.refcounterref.application.port.ReferenceAppointmentInfoStorage;
import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.domain.reference.ReferenceInstitutionBo;
import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.AppointmentDataDto;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceAppointmentInfoStorageImpl implements ReferenceAppointmentInfoStorage {

	private static final Short APPOINTMENT_CANCELLED_STATE = 4;

	private final LocalDateMapper localDateMapper;

	private final SharedAppointmentPort sharedAppointmentPort;

	private final ReferenceAppointmentStorage referenceAppointmentStorage;

	@Override
	public Optional<ReferenceAppointmentBo> getAppointmentData(Integer referenceId) {
		log.debug("Fetch appointment data by referenceId -> referenceId {} ", referenceId);
		Map<Integer, List<Integer>> appointments = referenceAppointmentStorage.getReferenceAppointmentsIdsWithoutCancelledStateId(Collections.singletonList(referenceId));
		if (!appointments.isEmpty()) {
			Optional<AppointmentDataDto> appointmentData = sharedAppointmentPort.getNearestAppointmentData(appointments.get(referenceId));
			if (appointmentData.isPresent())
				return Optional.of(mapFromAppointmentDataDto(appointmentData.get()));
		}
		return Optional.empty();
	}

	private ReferenceAppointmentBo mapFromAppointmentDataDto(AppointmentDataDto appointmentData) {
		return ReferenceAppointmentBo.builder()
				.appointmentId(appointmentData.getAppointmentId())
				.appointmentStateId(appointmentData.getState())
				.institution(new ReferenceInstitutionBo(appointmentData.getInstitution().getId(), appointmentData.getInstitution().getName()))
				.date(localDateMapper.fromDateDto(appointmentData.getDate()).atTime(localDateMapper.fromTimeDto(appointmentData.getHour())))
				.phonePrefix(appointmentData.getPhonePrefix())
				.phoneNumber(appointmentData.getPhoneNumber())
				.professionalFullName(appointmentData.getProfessionalFullName())
				.email(appointmentData.getPatientEmail())
				.authorFullName(appointmentData.getAuthorFullName())
				.createdOn(localDateMapper.fromDateTimeDto(appointmentData.getCreatedOn()))
				.build();
	}
}
