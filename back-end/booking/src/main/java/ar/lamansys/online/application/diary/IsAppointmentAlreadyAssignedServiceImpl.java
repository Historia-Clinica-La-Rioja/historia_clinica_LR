package ar.lamansys.online.application.diary;

import ar.lamansys.online.infraestructure.output.repository.diary.DiaryBookingDataStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class IsAppointmentAlreadyAssignedServiceImpl implements IsAppointmentAlreadyAssignedService {

	private DiaryBookingDataStorage diaryBookingDataStorage;

	@Override
	public boolean run(Integer diaryId, Integer openingHoursId, String date, String time) {
		log.debug("Input parameters -> diaryId {}, openingHoursId {}, date {}, time {}", diaryId, openingHoursId, date, time);
		boolean result = diaryBookingDataStorage.fetchIfAppointmentAlreadyAssigned(diaryId, openingHoursId, date, time);
		log.debug("Output -> {}", result);
		return result;
	}

}
