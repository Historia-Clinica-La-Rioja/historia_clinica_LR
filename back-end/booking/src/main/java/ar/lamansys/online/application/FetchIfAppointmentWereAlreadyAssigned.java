package ar.lamansys.online.application;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@AllArgsConstructor
@Service
public class FetchIfAppointmentWereAlreadyAssigned {

	private SharedAppointmentPort sharedAppointmentPort;

	public Boolean run(Integer diaryId, Integer openingHoursId, LocalDate appointmentDate, LocalTime appointmentTime) {
		log.debug("Input parameters -> diaryId {}, openingHoursId {}, appointmentDate {}, appointmentTime {}", diaryId, openingHoursId, appointmentDate, appointmentTime);
		boolean result = sharedAppointmentPort.appointmentDateAndTimeAlreadyUsed(diaryId, openingHoursId, appointmentDate, appointmentTime);
		log.debug("Output -> {}", result);
		return result;
	}

}
