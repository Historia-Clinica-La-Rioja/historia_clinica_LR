package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface DiaryOpeningHoursService {

    void load(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours);
    
    void update(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours);

    List<OccupationBo> findAllWeeklyDoctorsOfficeOccupation(Integer doctorOfficeId, LocalDate startDate, LocalDate endDate, Integer ignoreDiaryId);

    Collection<DiaryOpeningHoursBo> getDiariesOpeningHours(List<Integer> diaryIds);

    List<Short> overlappingDays(@NotNull LocalDate rangeStart1, @NotNull LocalDate rangeEnd1,
                                @NotNull LocalDate rangeStart2, @NotNull LocalDate rangeEnd2);
}
