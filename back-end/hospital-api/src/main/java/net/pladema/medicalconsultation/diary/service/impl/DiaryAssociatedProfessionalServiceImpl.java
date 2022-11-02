package net.pladema.medicalconsultation.diary.service.impl;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.diary.repository.DiaryAssociatedProfessionalRepository;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryAssociatedProfessional;
import net.pladema.medicalconsultation.diary.service.DiaryAssociatedProfessionalService;

import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryAssociatedProfessionalServiceImpl implements DiaryAssociatedProfessionalService {

	private final DiaryAssociatedProfessionalRepository diaryAssociatedProfessionalsRepository;

	@Override
	public void updateDiaryAssociatedProfessionals(List<Integer> associatedProfessionalsId, Integer diaryId) {
		List<DiaryAssociatedProfessional> diaryAssociatedProfessionals = diaryAssociatedProfessionalsRepository.getDiaryAssociatedProfessionalsByDiary(diaryId);
		associatedProfessionalsId.forEach(associatedProfessionalId -> {
			if (!getDiaryAssociatedProfessionalsIds(diaryAssociatedProfessionals).contains(associatedProfessionalId))
				addDiaryAssociatedProfessional(associatedProfessionalId, diaryId);
		});
		diaryAssociatedProfessionals.forEach(diaryAssociatedProfessional -> {
			if (!associatedProfessionalsId.contains(diaryAssociatedProfessional.getHealthcareProfessionalId())) {
				deleteDiaryAssociatedProfessionals(diaryAssociatedProfessional);
			}
		});
	}

	@Override
	public List<ProfessionalPersonBo> getAllDiaryAssociatedProfessionalsInfo(Integer diaryId) {
		return diaryAssociatedProfessionalsRepository.getDiaryAssociatedProfessionalsInfo(diaryId);
	}

	@Override
	public List<Integer> getAllAssociatedWithProfessionalsByHealthcareProfessionalId(Integer institutionId, Integer healthcareProfessionalId) {
		return diaryAssociatedProfessionalsRepository.getAllAssociatedWithHealthcareProfessionalsIdByHealthcareProfessional(institutionId, healthcareProfessionalId);
	}

	@Override
	public List<Integer> getAllAssociatedWithProfessionalsByHealthcareProfessionalIdAndActiveDiaries(Integer institutionId, Integer healthcareProfessionalId) {
		return diaryAssociatedProfessionalsRepository.getAllAssociatedWithHealthcareProfessionalsIdAndActiveDiariesByHealthcareProfessional(institutionId, healthcareProfessionalId);
	}


	private List<Integer> getDiaryAssociatedProfessionalsIds(List<DiaryAssociatedProfessional> diaryAssociatedProfessionals) {
		return diaryAssociatedProfessionals.stream()
				.map(DiaryAssociatedProfessional::getHealthcareProfessionalId)
				.collect(Collectors.toList());
	}

	private void deleteDiaryAssociatedProfessionals(DiaryAssociatedProfessional associatedProfessional) {
		diaryAssociatedProfessionalsRepository.delete(associatedProfessional);
		diaryAssociatedProfessionalsRepository.flush();
	}

	private void addDiaryAssociatedProfessional(Integer diaryAssociatedProfessionalId, Integer diaryId) {
		DiaryAssociatedProfessional diaryAssociatedProfessional = new DiaryAssociatedProfessional();
		diaryAssociatedProfessional.setDiaryId(diaryId);
		diaryAssociatedProfessional.setHealthcareProfessionalId(diaryAssociatedProfessionalId);
		diaryAssociatedProfessionalsRepository.save(diaryAssociatedProfessional);
	}

}
