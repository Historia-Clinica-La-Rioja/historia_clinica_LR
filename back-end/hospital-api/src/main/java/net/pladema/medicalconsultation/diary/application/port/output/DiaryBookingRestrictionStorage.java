package net.pladema.medicalconsultation.diary.application.port.output;

import net.pladema.medicalconsultation.diary.service.domain.DiaryBookingRestrictionBo;

public interface DiaryBookingRestrictionStorage {
    void save(Integer diaryId, DiaryBookingRestrictionBo restrictionBo);
}
