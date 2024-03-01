package ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment;

import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;

import ar.lamansys.refcounterref.domain.reference.ReferenceInstitutionBo;
import ar.lamansys.refcounterref.domain.reference.ReferencePhoneBo;
import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.ReferenceRepository;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceAppointmentStorageImpl implements ReferenceAppointmentStorage {

	private static final Short APPOINTMENT_CANCELLED_STATE = 4;

	private final ReferenceAppointmentRepository referenceAppointmentRepository;

	private final SharedAppointmentPort sharedAppointmentPort;

	private final ReferenceRepository referenceRepository;

	private final LocalDateMapper localDateMapper;

	@Override
	public void save(Integer referenceId, Integer appointmentId, Boolean alreadyHasPhone) {
		log.debug("Input parameters -> referenceId {}, appointmentId {}, alreadyHasPhone {}", referenceId, appointmentId, alreadyHasPhone);
		var diaryId = sharedAppointmentPort.getDiaryId(appointmentId);
		var allowedProtectedAppointment = sharedAppointmentPort.openingHourAllowedProtectedAppointments(appointmentId, diaryId);
		referenceAppointmentRepository.save(new ReferenceAppointment(referenceId, appointmentId, allowedProtectedAppointment));
		if (!alreadyHasPhone) {
			ReferencePhoneBo referencePhoneData = referenceRepository.getReferencePhoneData(referenceId);
			sharedAppointmentPort.updateAppointmentPhoneNumber(appointmentId, referencePhoneData.getPhonePrefix(), referencePhoneData.getPhoneNumber());
		}
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
	public Optional<ReferenceAppointmentBo> getAppointmentData(Integer referenceId) {
		log.debug("Fetch appointment data by referenceId -> referenceId {} ", referenceId);
		Map<Integer, List<Integer>> appointments = this.getReferenceAppointmentsIdsWithoutCancelledStateId(Collections.singletonList(referenceId));
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
				.build();
	}

}
