package net.pladema.staff.application.updatehealthcareprofessional.exceptions;

import lombok.Getter;

@Getter
public class UpdateHealthcareProfessionalException extends RuntimeException {

    private final UpdateHealthcareProfessionalEnumException code;

    public UpdateHealthcareProfessionalException(UpdateHealthcareProfessionalEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

