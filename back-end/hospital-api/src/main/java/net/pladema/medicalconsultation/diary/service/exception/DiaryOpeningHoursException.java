package net.pladema.medicalconsultation.diary.service.exception;

import lombok.Getter;

@Getter
public class DiaryOpeningHoursException extends RuntimeException {

    private static final long serialVersionUID = 6001787289196762314L;
    private final DiaryOpeningHoursEnumException code;

    public DiaryOpeningHoursException(DiaryOpeningHoursEnumException code, String message) {
        super(message);
        this.code = code;
    }
}