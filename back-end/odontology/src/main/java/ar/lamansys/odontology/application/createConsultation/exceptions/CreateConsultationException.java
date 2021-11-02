package ar.lamansys.odontology.application.createConsultation.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateConsultationException extends RuntimeException {

    private final CreateConsultationExceptionEnum code;

    private final List<String> messages = new ArrayList<>();

    public CreateConsultationException(CreateConsultationExceptionEnum code, String message) {
        super();
        this.code = code;
        this.messages.add(message);
    }

    public CreateConsultationException(CreateConsultationExceptionEnum code) {
        super();
        this.code = code;
    }

    public boolean hasErrors() {
        return !this.messages.isEmpty();
    }

    public void addError(String message) {
        this.messages.add(message);
    }

    public String getCode() {
        return code.name();
    }

}