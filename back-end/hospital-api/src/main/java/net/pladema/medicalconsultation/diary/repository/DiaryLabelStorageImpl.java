package net.pladema.medicalconsultation.diary.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.service.DiaryLabelStorage;

import net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DiaryLabelStorageImpl implements DiaryLabelStorage {

	private static final String OUTPUT = "Output -> {}";

	private final DiaryLabelRepository diaryLabelRepository;

	@Override
	public List<DiaryLabelBo> getDiaryLabels(Integer diaryId) {
		log.debug("Input parameters -> diaryId {}", diaryId);
		List<DiaryLabelBo> result = diaryLabelRepository.getDiaryLabels(diaryId);
		log.debug(OUTPUT, result);
		return result;
	}
}
