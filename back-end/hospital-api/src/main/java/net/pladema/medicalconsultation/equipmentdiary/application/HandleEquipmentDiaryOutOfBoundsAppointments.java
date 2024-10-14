package net.pladema.medicalconsultation.equipmentdiary.application;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentPort;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.equipmentdiary.application.port.output.EquipmentDiaryPort;

import net.pladema.medicalconsultation.equipmentdiary.domain.UpdateEquipmentDiaryAppointmentBo;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentOpeningHoursBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@RequiredArgsConstructor
@Service
public class HandleEquipmentDiaryOutOfBoundsAppointments {

	private final EquipmentDiaryPort equipmentDiaryPort;

	private final AppointmentPort appointmentPort;

	private final AppointmentService appointmentService;

	@Transactional
	public void run(EquipmentDiaryBo diaryToUpdate) {
		log.debug("Input parameters -> diary {}", diaryToUpdate);

		List<UpdateEquipmentDiaryAppointmentBo> appointments = equipmentDiaryPort.getUpdateEquipmentDiaryAppointments(diaryToUpdate.getId());

		LocalDate from = diaryToUpdate.getStartDate();
		LocalDate to = diaryToUpdate.getEndDate();

		HashMap<Short, List<EquipmentDiaryOpeningHoursBo>> appointmentsByWeekday = diaryToUpdate.getDiaryOpeningHours()
				.stream()
				.collect(groupingBy(doh -> doh.getOpeningHours().getDayWeekId(), HashMap<Short, List<EquipmentDiaryOpeningHoursBo>>::new, Collectors.toList()));

		appointments.stream()
				.filter(a -> {
					List<EquipmentDiaryOpeningHoursBo> newHours = appointmentsByWeekday.get(getWeekDay(a.getDate()));
					return newHours == null
							|| outOfDiaryBounds(from, to, a)
							|| outOfOpeningHoursBounds(a, newHours);
				})
				.forEach(this::changeToOutOfDiaryState);

		log.debug("Output -> updated appointments with out-of-diary state");
	}

	private void changeToOutOfDiaryState(UpdateEquipmentDiaryAppointmentBo appointmentBo) {
		if (AppointmentState.BLOCKED == appointmentBo.getStateId()) {
			appointmentPort.deleteAppointmentById(appointmentBo.getId());
			return;
		}
		if (appointmentBo.getDate().isAfter(LocalDate.now()))
			appointmentService.updateState(appointmentBo.getId(), AppointmentState.OUT_OF_DIARY, UserInfo.getCurrentAuditor(), "Fuera de agenda");
	}

	private boolean outOfOpeningHoursBounds(UpdateEquipmentDiaryAppointmentBo a, List<EquipmentDiaryOpeningHoursBo> newHours) {
		return newHours.stream().noneMatch(newOH -> fitsIn(a, newOH.getOpeningHours()) && sameMedicalAttention(a, newOH));
	}

	private boolean sameMedicalAttention(UpdateEquipmentDiaryAppointmentBo a, EquipmentDiaryOpeningHoursBo newOH) {
		return newOH.getMedicalAttentionTypeId().equals(a.getMedicalAttentionTypeId());
	}

	private boolean outOfDiaryBounds(LocalDate from, LocalDate to, UpdateEquipmentDiaryAppointmentBo a) {
		return !isBetween(from, to, a);
	}

	private boolean isBetween(LocalDate from, LocalDate to, UpdateEquipmentDiaryAppointmentBo a) {
		return !a.getDate().isBefore(from) && !a.getDate().isAfter(to);
	}

	private boolean fitsIn(UpdateEquipmentDiaryAppointmentBo appointment, EquipmentOpeningHoursBo openingHours) {
		LocalTime from = openingHours.getFrom();
		LocalTime to = openingHours.getTo();
		return (appointment.getTime().equals(from) || appointment.getTime().isAfter(from)) && appointment.getTime().isBefore(to);
	}

}
