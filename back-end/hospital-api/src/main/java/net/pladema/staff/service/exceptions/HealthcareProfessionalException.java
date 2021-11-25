package net.pladema.staff.service.exceptions;

import lombok.Getter;


@Getter
public class HealthcareProfessionalException extends RuntimeException {

    private final HealthcareProfessionalEnumException code;

    public HealthcareProfessionalException(HealthcareProfessionalEnumException code, String message) {
        super(message);
        this.code = code;
    }
}