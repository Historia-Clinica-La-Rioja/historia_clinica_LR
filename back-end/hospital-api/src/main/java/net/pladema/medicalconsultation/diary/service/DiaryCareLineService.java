package net.pladema.medicalconsultation.diary.service;

import net.pladema.establishment.service.domain.CareLineBo;

import java.util.List;

public interface DiaryCareLineService {

	void updateCareLinesAssociatedToDiary(Integer diaryId, List<Integer> careLines);

	List<CareLineBo> getPossibleCareLinesForDiary(Integer institutionId, Integer clinicalSpecialtyId);

	List<CareLineBo> getAllCareLinesByDiaryId(Integer diaryId, Integer healthCareProfessionalId);

	List<CareLineBo> getPossibleCareLinesForDiaryByPracticesAndSpecialty(Integer institutionId, List<Integer> practicesId, Integer clinicalSpecialtyId);

	List<Integer> getDiaryIdsByCareLineId(Integer careLineId, Integer institutionId);

}
