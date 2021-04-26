package net.pladema.medicalconsultation.diary.service.exception;

import lombok.Getter;

@Getter
public enum DiaryOpeningHoursEnumException {
    NULL_DOCTOR_OFFICE_ID,
    DIARY_END_DATE_BEFORE_START_DATE,
    ;

}
