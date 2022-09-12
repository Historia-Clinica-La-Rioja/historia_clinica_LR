package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface DiaryOpeningHoursService {

    void load(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours, List<DiaryOpeningHoursBo>... oldOpeningHours);
    
    void update(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours);

    List<OccupationBo> findAllWeeklyDoctorsOfficeOccupation(Integer doctorOfficeId, LocalDate startDate, LocalDate endDate, Integer ignoreDiaryId) throws DiaryOpeningHoursException;

	Collection<DiaryOpeningHoursBo> getDiariesOpeningHours(List<Integer> diaryIds);
	Collection<DiaryOpeningHoursBo> getDiaryOpeningHours(Integer diaryId);

    List<Short> overlappingDays(@NotNull LocalDate rangeStart1, @NotNull LocalDate rangeEnd1,
                                @NotNull LocalDate rangeStart2, @NotNull LocalDate rangeEnd2);
}
