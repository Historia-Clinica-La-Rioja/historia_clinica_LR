package net.pladema.establishment.controller.exceptions;

import lombok.Getter;

@Getter
public class BackofficeMedicalCoverageException extends RuntimeException {

    private final BackofficeMedicalCoverageEnumException code;

    public BackofficeMedicalCoverageException(BackofficeMedicalCoverageEnumException code, String message) {
        super(message);
        this.code = code;
    }
}