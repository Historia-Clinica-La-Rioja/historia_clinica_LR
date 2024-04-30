package ar.lamansys.online.application;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.diaryOpeningHours.SharedDiaryOpeningHoursPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class FetchIfOpeningHoursAllowsWebAppointments {

	private SharedDiaryOpeningHoursPort sharedDiaryOpeningHoursPort;

	public Boolean run(Integer diaryId, Integer openingHoursId) {
		log.debug("Input parameters -> diaryId {}, openingHoursId {}", diaryId, openingHoursId);
		Boolean result = sharedDiaryOpeningHoursPort.fetchIfOpeningHoursAllowWebAppointments(diaryId, openingHoursId);
		log.debug("Output -> {}", result);
		return result;
	}

}
