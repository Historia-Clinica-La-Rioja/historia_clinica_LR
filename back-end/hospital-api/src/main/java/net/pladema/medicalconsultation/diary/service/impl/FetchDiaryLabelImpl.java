package net.pladema.medicalconsultation.diary.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.service.DiaryLabelStorage;
import net.pladema.medicalconsultation.diary.service.FetchDiaryLabel;

import net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FetchDiaryLabelImpl implements FetchDiaryLabel {

	private static final String OUTPUT = "Output -> {}";

	private final DiaryLabelStorage diaryLabelStorage;

	@Override
	public List<DiaryLabelBo> run(Integer diaryId) {
		log.debug("Input parameters -> diaryId {}", diaryId);
		List<DiaryLabelBo> result = diaryLabelStorage.getDiaryLabels(diaryId);
		log.debug(OUTPUT, result);
		return result;
	}
}
