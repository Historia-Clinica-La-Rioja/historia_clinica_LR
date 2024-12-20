package net.pladema.medicalconsultation.diary.application.exceptions;

import lombok.Getter;

@Getter
public class DiaryBookingRestrictionException extends RuntimeException {

    public final DiaryBookingRestrictionExceptionEnum code;

    public DiaryBookingRestrictionException(DiaryBookingRestrictionExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}
