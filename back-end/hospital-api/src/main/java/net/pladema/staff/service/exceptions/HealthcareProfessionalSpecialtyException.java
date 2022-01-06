package net.pladema.staff.service.exceptions;

import lombok.Getter;

@Getter
public class HealthcareProfessionalSpecialtyException extends RuntimeException {

    private final HealthcareProfessionalSpecialtyEnumException code;

    public HealthcareProfessionalSpecialtyException(HealthcareProfessionalSpecialtyEnumException code, String message) {
        super(message);
        this.code = code;
    }
}
