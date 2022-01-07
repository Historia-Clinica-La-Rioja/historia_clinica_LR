package ar.lamansys.refcounterref.application.createcounterreferencefile.exceptions;

import lombok.Getter;

@Getter
public class CreateCounterReferenceFileException extends RuntimeException {

    public final CreateCounterReferenceFileExceptionEnum code;

    public CreateCounterReferenceFileException(CreateCounterReferenceFileExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }

}