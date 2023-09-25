package net.pladema.medicalconsultation.diary.service;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;

import java.util.List;

public interface DiaryPracticeService {

	void updateDiaryPractices(List<Integer> snomedRelatedGroupIds, Integer diaryId);

	List<SnomedBo> getAllByDiaryId(Integer diaryId);

	List<SnomedBo> getPracticesByActiveDiaries(Integer institutionId);

	boolean hasPractice(Integer diaryId);
}
