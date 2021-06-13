package net.pladema.medicalconsultation.diary.service.exception;

import lombok.Getter;

@Getter
public class DiaryOpeningHoursException extends Exception {

    private final DiaryOpeningHoursEnumException code;

    public DiaryOpeningHoursException(DiaryOpeningHoursEnumException code, String mensajeError) {
        super(mensajeError);
        this.code = code;
    }
}