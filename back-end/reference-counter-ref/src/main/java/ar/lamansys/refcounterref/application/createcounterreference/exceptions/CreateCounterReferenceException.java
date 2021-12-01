package ar.lamansys.refcounterref.application.createcounterreference.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateCounterReferenceException extends RuntimeException {

    public final CreateCounterReferenceExceptionEnum code;

    public CreateCounterReferenceException(CreateCounterReferenceExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }

}