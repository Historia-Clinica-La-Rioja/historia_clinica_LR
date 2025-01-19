package net.pladema.medicalconsultation.diary.application.port.output;

import net.pladema.medicalconsultation.diary.service.domain.DiaryBookingRestrictionBo;

import java.util.Optional;

public interface DiaryBookingRestrictionStorage {
    void save(Integer diaryId, DiaryBookingRestrictionBo restrictionBo);

    Optional<DiaryBookingRestrictionBo> getByDiaryId(Integer diaryId);
}
