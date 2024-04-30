package ar.lamansys.online.application;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.diaryOpeningHours.SharedDiaryOpeningHoursPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@AllArgsConstructor
@Service
public class FetchIfAppointmentCanBeAssignedAsOverturn {

	private SharedDiaryOpeningHoursPort sharedDiaryOpeningHoursPort;

	public boolean run(Integer diaryId, Integer openingHoursId, LocalDate appointmentDate) {
		log.debug("Input parameters -> diaryId {}, openingHoursId {}, appointmentDate {}", diaryId, openingHoursId, appointmentDate);
		boolean result = sharedDiaryOpeningHoursPort.fetchIfAppointmentCanBeAssignedAsOverturn(diaryId, openingHoursId, appointmentDate);
		log.debug("Output -> {}", result);
		return result;
	}

}
