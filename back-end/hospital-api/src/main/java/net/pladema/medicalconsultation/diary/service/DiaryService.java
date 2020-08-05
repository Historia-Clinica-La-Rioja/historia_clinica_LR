package net.pladema.medicalconsultation.diary.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

public interface DiaryService {

    Integer addDiary(DiaryBo diaryToSave);

    List<Integer> getAllOverlappingDiary(Integer healthcareProfessionalId, Integer doctorsOfficeId,
                                         LocalDate newDiaryStart, LocalDate newDiaryEnd);

    Collection<DiaryBo> getActiveDiariesFromProfessional(Integer healthcareProfessionalId);
    
    Optional<CompleteDiaryBo> getDiary(Integer diaryId);

}
