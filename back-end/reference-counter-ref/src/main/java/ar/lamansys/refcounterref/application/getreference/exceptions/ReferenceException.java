package ar.lamansys.refcounterref.application.getreference.exceptions;

import lombok.Getter;

@Getter
public class ReferenceException extends RuntimeException {

    public final ReferenceExceptionEnum code;

    public ReferenceException(ReferenceExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }

}