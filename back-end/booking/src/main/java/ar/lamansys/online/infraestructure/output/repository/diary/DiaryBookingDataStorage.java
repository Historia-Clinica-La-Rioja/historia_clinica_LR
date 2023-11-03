package ar.lamansys.online.infraestructure.output.repository.diary;

import ar.lamansys.online.domain.diary.DiaryBasicDataBo;
import ar.lamansys.online.domain.diary.OpeningHoursBasicDataBo;

import java.util.List;
import java.util.Optional;

public interface DiaryBookingDataStorage {

	DiaryBasicDataBo fetchDiaryBasicDataByDiaryId(Integer diaryId);

	boolean fetchIfAppointmentAlreadyAssigned(Integer diaryId, Integer openingHoursId, String date, String time);

}
