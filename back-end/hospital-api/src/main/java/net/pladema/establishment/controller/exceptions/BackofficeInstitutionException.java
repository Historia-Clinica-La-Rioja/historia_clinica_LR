package net.pladema.establishment.controller.exceptions;

import lombok.Getter;

@Getter
public class BackofficeInstitutionException extends RuntimeException {

    private final BackofficeInstitutionEnumException code;

    public BackofficeInstitutionException(BackofficeInstitutionEnumException code,String message) {
        super(message);
        this.code = code;
    }
}
