package net.pladema.medicalconsultation.diary.service;

import net.pladema.establishment.service.domain.CareLineBo;
import java.util.List;

import java.util.List;

public interface DiaryCareLineService {

	void updateCareLinesAssociatedToDiary(Integer diaryId, List<Integer> careLines);
	
	List<CareLineBo> getAllCareLinesByDiaryId(Integer diaryId, Integer healthCareProfessionalId);

}
