package net.pladema.staff.application.saveprofessional.exceptions;

import lombok.Getter;

@Getter
public class CreateHealthcareProfessionalSpecialtyException extends RuntimeException {

    private final CreateHealthcareProfessionalSpecialtyEnumException code;

    public CreateHealthcareProfessionalSpecialtyException(CreateHealthcareProfessionalSpecialtyEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

