package ar.lamansys.odontology.application.createConsultation.exceptions;

import lombok.Getter;

@Getter
public class CreateConsultationException extends RuntimeException{

    public final CreateConsultationExceptionEnum code;

    public CreateConsultationException(CreateConsultationExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}