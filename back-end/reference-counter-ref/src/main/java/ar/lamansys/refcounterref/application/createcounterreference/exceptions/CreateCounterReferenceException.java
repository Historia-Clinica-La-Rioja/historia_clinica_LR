package ar.lamansys.refcounterref.application.createcounterreference.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateCounterReferenceException extends RuntimeException {

    public final CreateCounterReferenceExceptionEnum code;
    private final List<String> messages = new ArrayList<>();

    public CreateCounterReferenceException(CreateCounterReferenceExceptionEnum code, String message) {
        super(message);
        this.code = code;
        this.messages.add(message);
    }

    public CreateCounterReferenceException(CreateCounterReferenceExceptionEnum code) {
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