package net.pladema.medicalconsultation.diary.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateDiaryAndAppointments {

    private final UpdateOutOfBoundsAppointments updateOutOfBoundsAppointments;
    private final DiaryService diaryService;

    @Transactional
    public Integer run(DiaryBo diaryBo) {
        log.debug("Input parameters -> diaryBo {}", diaryBo);

        updateOutOfBoundsAppointments.run(diaryBo);
        Integer result = diaryService.updateDiary(diaryBo);

        log.debug("Output -> result {}", result);
        return result;
    }
}
