package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;

import java.time.LocalDate;
import java.util.List;

public interface DiaryOpeningHoursValidatorService {

    boolean allowNewOverturn(Integer diaryId, Integer openingHoursId, LocalDate newApmtDate);

    boolean overlapDiaryOpeningHours(DiaryBo diaryBo, List<DiaryOpeningHoursBo> openingHours);
}

