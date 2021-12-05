package ar.lamansys.nursing.application.exceptions;

import lombok.Getter;

@Getter
public class NursingConsultationException extends RuntimeException {

    public final NursingConsultationExceptionEnum code;

    public NursingConsultationException(NursingConsultationExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }

}

