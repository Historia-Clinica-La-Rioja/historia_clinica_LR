package net.pladema.medicalconsultation.diary.service;

import java.util.List;

public interface DiaryPracticeService {

	void updateDiaryPractices(List<Integer> snomedRelatedGroupIds, Integer diaryId);
}
