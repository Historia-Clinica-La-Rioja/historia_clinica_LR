package ar.lamansys.refcounterref.application.getreferencefile.exceptions;

import lombok.Getter;

@Getter
public class GetReferenceFileException extends RuntimeException {

    public final GetReferenceFileExceptionEnum code;

    public GetReferenceFileException(GetReferenceFileExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }

}