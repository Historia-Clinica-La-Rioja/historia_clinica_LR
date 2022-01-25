package ar.lamansys.refcounterref.application.getcounterreferencefile.exceptions;

import lombok.Getter;

@Getter
public class GetCounterReferenceFileException extends RuntimeException {

    public final GetCounterReferenceFileExceptionEnum code;

    public GetCounterReferenceFileException(GetCounterReferenceFileExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }


}
