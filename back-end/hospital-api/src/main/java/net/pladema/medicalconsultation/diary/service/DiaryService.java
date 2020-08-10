package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DiaryService {

    Integer addDiary(DiaryBo diaryToSave);

    List<Integer> getAllOverlappingDiary(Integer healthcareProfessionalId, Integer doctorsOfficeId,
                                         LocalDate newDiaryStart, LocalDate newDiaryEnd);

    Collection<DiaryBo> getActiveDiariesFromProfessional(Integer healthcareProfessionalId);
    
    Optional<CompleteDiaryBo> getDiary(Integer diaryId);

    DiaryBo getDiaryById(Integer diaryId);
}
