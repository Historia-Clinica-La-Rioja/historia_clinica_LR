package net.pladema.medicalconsultation.diary.service.impl;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.diary.repository.DiaryAssociatedProfessionalRepository;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryAssociatedProfessional;
import net.pladema.medicalconsultation.diary.service.DiaryAssociatedProfessionalService;

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
		diaryAssociatedProfessionals.forEach(diaryAssociatedProfessional -> {
			if (!associatedProfessionalsId.contains(diaryAssociatedProfessional.getHealthcareProfessionalId())) {
				deleteDiaryAssociatedProfessionals(diaryAssociatedProfessional);
			}
		});
		associatedProfessionalsId.forEach(associatedProfessionalId -> {
			if (!getDiaryAssociatedProfessionalsIds(diaryAssociatedProfessionals).contains(associatedProfessionalId))
				addDiaryAssociatedProfessional(associatedProfessionalId, diaryId);
		});
	}

	@Override
	public List<Integer> getAllDiaryAssociatedProfessionals(Integer diaryId) {
		List<DiaryAssociatedProfessional> diaryAssociatedProfessionals = diaryAssociatedProfessionalsRepository.getDiaryAssociatedProfessionalsByDiary(diaryId);
		return getDiaryAssociatedProfessionalsIds(diaryAssociatedProfessionals);
	}

	@Override
	public List<Integer> getAllAssociatedWithProfessionalsByHealthcareProfessionalId(Integer healthcareProfessionalId) {
		return diaryAssociatedProfessionalsRepository.getAllAssociatedWithHealthcareProfessionalsIdByHealthcareProfessional(healthcareProfessionalId);
	}


	private List<Integer> getDiaryAssociatedProfessionalsIds(List<DiaryAssociatedProfessional> diaryAssociatedProfessionals) {
		return diaryAssociatedProfessionals.stream()
				.map(DiaryAssociatedProfessional::getHealthcareProfessionalId)
				.collect(Collectors.toList());
	}

	private void deleteDiaryAssociatedProfessionals(DiaryAssociatedProfessional associatedProfessional) {
		diaryAssociatedProfessionalsRepository.delete(associatedProfessional);
	}

	private void addDiaryAssociatedProfessional(Integer diaryAssociatedProfessionalId, Integer diaryId) {
		DiaryAssociatedProfessional diaryAssociatedProfessional = new DiaryAssociatedProfessional();
		diaryAssociatedProfessional.setDiaryId(diaryId);
		diaryAssociatedProfessional.setHealthcareProfessionalId(diaryAssociatedProfessionalId);
		diaryAssociatedProfessionalsRepository.save(diaryAssociatedProfessional);
	}

}
