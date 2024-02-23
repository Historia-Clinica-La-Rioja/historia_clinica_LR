package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.GroupAppointmentRepository;

import net.pladema.medicalconsultation.appointment.service.domain.GroupAppointmentBo;

import net.pladema.medicalconsultation.appointment.service.domain.GroupAppointmentResponseBo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class GetAppointmentsFromDeterminatedDiaryDateTime {

	private final GroupAppointmentRepository groupAppointmentRepository;

	private final LocalDateMapper localDateMapper;

	public List<GroupAppointmentResponseBo> run(GroupAppointmentBo groupAppointmentBo) {
		log.debug("Input parameters -> groupAppointmentBo {}", groupAppointmentBo);
		LocalDateTime localDateTime = localDateMapper.fromDateTimeDto(groupAppointmentBo.getDate());
		LocalDate date = localDateTime.toLocalDate();
		LocalTime hour = localDateTime.toLocalTime();
		return groupAppointmentRepository.getApppointmentsFromDeterminatedDiaryDateTime(
				groupAppointmentBo.getDiaryId(),
				date,
				hour
		);
	}
}
