package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DiaryService {

	Integer addDiary(DiaryBo diaryToSave);

	Integer updateDiary(DiaryBo diaryToSave);
	
	Boolean deleteDiary(Integer diaryId);

	List<Integer> getAllOverlappingDiaryByProfessional(Integer healthcareProfessionalId, Integer doctorsOfficeId,
													   LocalDate newDiaryStart, LocalDate newDiaryEnd, Optional<Integer> excludeDiaryId);

    List<DiaryBo> getAllOverlappingDiary(Integer doctorsOfficeId,
                                         LocalDate newDiaryStart, LocalDate newDiaryEnd, Optional<Integer> excludeDiaryId);

    Collection<DiaryBo> getActiveDiariesFromProfessional(Integer healthcareProfessionalId);

	Optional<CompleteDiaryBo> getDiary(Integer diaryId);
	
	Optional<DiaryBo> getDiaryByAppointment(Integer appointmentId);

	DiaryBo getDiaryById(Integer diaryId);

}
