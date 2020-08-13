package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface DiaryOpeningHoursService {

    void load(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours);
    
    void update(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours);

    List<OccupationBo> findAllWeeklyDoctorsOfficeOccupation(Integer doctorOfficeId, LocalDate startDate, LocalDate endDate, Integer ignoreDiaryId);

    Collection<DiaryOpeningHoursBo> getDiariesOpeningHours(List<Integer> diaryIds);

    boolean allowNewOverturn(Integer diaryId, Integer openingHoursId, LocalDate newApmtDate);
}
