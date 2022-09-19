package net.pladema.medicalconsultation.diary.service;

import java.util.List;

public interface DiaryAssociatedProfessionalService {

	void updateDiaryAssociatedProfessionals(List<Integer> associatedProfessionalsId, Integer diaryId);

	List<Integer> getAllDiaryAssociatedProfessionals(Integer diaryId);

	List<Integer> getAllAssociatedWithProfessionalsByHealthcareProfessionalId(Integer healthcareProfessionalId);

}
