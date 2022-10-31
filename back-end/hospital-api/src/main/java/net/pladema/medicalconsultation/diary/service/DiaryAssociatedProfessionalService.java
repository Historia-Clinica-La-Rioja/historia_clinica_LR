package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import java.util.List;

public interface DiaryAssociatedProfessionalService {

	void updateDiaryAssociatedProfessionals(List<Integer> associatedProfessionalsId, Integer diaryId);

	List<ProfessionalPersonBo> getAllDiaryAssociatedProfessionalsInfo(Integer diaryId);

	List<Integer> getAllAssociatedWithProfessionalsByHealthcareProfessionalId(Integer institutionId, Integer healthcareProfessionalId);

	List<Integer> getAllAssociatedWithProfessionalsByHealthcareProfessionalIdAndActiveDiaries(Integer institutionId, Integer healthcareProfessionalId);

}
