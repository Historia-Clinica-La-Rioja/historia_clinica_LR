package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo;

import java.util.List;

public interface DiaryLabelStorage {

	List<DiaryLabelBo> getDiaryLabels(Integer diaryId);
}
