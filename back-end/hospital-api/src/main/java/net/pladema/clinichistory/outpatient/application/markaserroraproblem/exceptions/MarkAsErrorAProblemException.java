package net.pladema.clinichistory.outpatient.application.markaserroraproblem.exceptions;

import lombok.Getter;

@Getter
public class MarkAsErrorAProblemException extends RuntimeException {
    private final MarkAsErrorAProblemExceptionEnum code;

    public MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}
