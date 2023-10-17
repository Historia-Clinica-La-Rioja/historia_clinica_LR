package ar.lamansys.online.application.diary;

import ar.lamansys.online.domain.diary.DiaryBasicDataBo;
import ar.lamansys.online.domain.diary.OpeningHoursBasicDataBo;
import ar.lamansys.online.infraestructure.output.repository.diary.DiaryBookingDataStorage;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class FetchOpeningHoursBasicDataServiceImpl implements FetchOpeningHoursBasicDataService {

	private DiaryBookingDataStorage diaryBookingDataStorage;

	@Override
	public DiaryBasicDataBo fetchDiaryBasicDataByDiaryId(Integer diaryId) {
		log.debug("Input parameters -> diaryId {}", diaryId);
		DiaryBasicDataBo result = diaryBookingDataStorage.fetchDiaryBasicDataByDiaryId(diaryId);
		log.debug("Output -> {}", result);
		return result;
	}

}
