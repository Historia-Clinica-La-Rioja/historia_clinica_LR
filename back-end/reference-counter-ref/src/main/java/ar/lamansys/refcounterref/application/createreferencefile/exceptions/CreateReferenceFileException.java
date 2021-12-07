package ar.lamansys.refcounterref.application.createreferencefile.exceptions;

import lombok.Getter;

@Getter
public class CreateReferenceFileException extends RuntimeException {

    public final CreateReferenceFileExceptionEnum code;

    public CreateReferenceFileException(CreateReferenceFileExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }

}