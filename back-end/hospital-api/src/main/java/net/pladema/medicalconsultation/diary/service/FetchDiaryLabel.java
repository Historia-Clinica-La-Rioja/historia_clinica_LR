package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo;

import java.util.List;

public interface FetchDiaryLabel {

	List<DiaryLabelBo> run(Integer diaryId);
}
