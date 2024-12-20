package net.pladema.medicalconsultation.diary.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryBookingRestrictionStorage;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBookingRestrictionBo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetDiaryBookingRestriction {

    private final DiaryBookingRestrictionStorage storage;

    public Optional<DiaryBookingRestrictionBo> run(Integer diaryId) {
        log.debug("Input parameters -> diaryId {}", diaryId);
        var result = storage.getByDiaryId(diaryId);
        log.debug("Output -> {}", Boolean.TRUE);
        return result;
    }
}
