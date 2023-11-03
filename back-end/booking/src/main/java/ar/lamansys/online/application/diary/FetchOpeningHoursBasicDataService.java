package ar.lamansys.online.application.diary;

import ar.lamansys.online.domain.diary.DiaryBasicDataBo;

public interface FetchOpeningHoursBasicDataService {

	DiaryBasicDataBo fetchDiaryBasicDataByDiaryId(Integer diaryId);

}
