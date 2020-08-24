package net.pladema.medicalconsultation.diary.service.impl;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.diary.repository.DiaryOpeningHoursRepository;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursValidatorService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class DiaryOpeningHoursValidatorServiceImpl implements DiaryOpeningHoursValidatorService {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryOpeningHoursValidatorServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DiaryOpeningHoursRepository diaryOpeningHoursRepository;

    private final DiaryService diaryService;

    private final DiaryOpeningHoursService diaryOpeningHoursService;

	@Override
	public boolean allowNewOverturn(Integer diaryId, Integer openingHoursId, LocalDate newApmtDate) {
		LOG.debug("Input parameters -> diaryId {}, openingHoursId {}", diaryId, openingHoursId);
		boolean result = diaryOpeningHoursRepository.allowNewOverturn(diaryId, openingHoursId, newApmtDate);
		LOG.debug(OUTPUT, result);
		return result;
    }

    @Override
    public boolean overlapDiaryOpeningHours(@NotNull DiaryBo diaryBo, @NotNull List<DiaryOpeningHoursBo> openingHours) {
        LOG.debug("Input parameters -> diaryBo {}, openingHours {} ", diaryBo, openingHours);
        boolean overlap = overlapBetweenDiaryOpeningHours(openingHours) ||
               overlapWithOthersDiaries(diaryBo, openingHours);
        LOG.debug(OUTPUT, overlap);
        return overlap;
    }

    private boolean overlapBetweenDiaryOpeningHours(@NotNull List<DiaryOpeningHoursBo> openingHours) {
        LOG.debug("Input parameters -> openingHours {}", openingHours);
        boolean overlap = false;

        int index = 0;
        DiaryOpeningHoursBo last = openingHours.get(index++);
        DiaryOpeningHoursBo current;

        while (index < openingHours.size() && !overlap) {
            current = openingHours.get(index++);
            overlap = last.overlap(current);
            last = current;
        }
        LOG.debug(OUTPUT, overlap);
        return overlap;
    }

    private boolean overlapWithOthersDiaries(@NotNull DiaryBo diaryBo, @NotNull  List<DiaryOpeningHoursBo> openingHours){
        LOG.debug("Input parameters -> diaryBo {}, openingHours {}", diaryBo, openingHours);
        List<DiaryBo> overlapDiaries = diaryService.getAllOverlappingDiary(diaryBo.getDoctorsOfficeId(), diaryBo.getStartDate(),
                diaryBo.getEndDate(), Optional.ofNullable(diaryBo.getId()));
        boolean overlap = overlapDiaries.stream().anyMatch(od ->
                    diaryBo.getDiaryOpeningHours().stream().anyMatch(doh -> overlapWithOthers(diaryBo, od, doh)));
        LOG.debug(OUTPUT, overlap);
        return overlap;
    }

    private boolean overlapWithOthers(@NotNull DiaryBo diaryBo, @NotNull DiaryBo overlapDiary, @NotNull DiaryOpeningHoursBo diaryOpeningHoursBo) {
        LOG.debug("Input parameters -> diaryBo {}, overlapDiary {}, diaryOpeningHoursBo {}", diaryBo, overlapDiary, diaryOpeningHoursBo);
        Objects.requireNonNull(diaryBo.getDoctorsOfficeId());
        Objects.requireNonNull(overlapDiary.getDoctorsOfficeId());
        Objects.requireNonNull(diaryOpeningHoursBo.getOpeningHours());
        Objects.requireNonNull(diaryOpeningHoursBo.getOpeningHours().getDayWeekId());

        if (!diaryBo.getDoctorsOfficeId().equals(overlapDiary.getDoctorsOfficeId())) {
            LOG.debug(OUTPUT, Boolean.FALSE);
            return Boolean.FALSE;
        }

        List<Short> overlappingDays = diaryOpeningHoursService.overlappingDays(diaryBo.getStartDate(),
                diaryBo.getEndDate(), overlapDiary.getStartDate(), overlapDiary.getEndDate());

        if (!overlappingDays.contains(diaryOpeningHoursBo.getOpeningHours().getDayWeekId())) {
            LOG.debug(OUTPUT, Boolean.FALSE);
            return Boolean.FALSE;
        }

        boolean result = diaryOpeningHoursRepository.overlapDiaryOpeningHoursFromOtherDiary(overlapDiary.getId(),
                overlapDiary.getDoctorsOfficeId(),
                diaryOpeningHoursBo.getOpeningHours().getDayWeekId(),
                diaryOpeningHoursBo.getOpeningHours().getFrom(),
                diaryOpeningHoursBo.getOpeningHours().getTo());
        LOG.debug(OUTPUT, result);
        return result;
    }


}
