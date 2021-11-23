package net.pladema.staff.application.createprofessional.exceptions;

public class CreateHealthcareProfessionalSpecialtyException extends RuntimeException {

    private final CreateHealthcareProfessionalSpecialtyEnumException code;

    public CreateHealthcareProfessionalSpecialtyException(CreateHealthcareProfessionalSpecialtyEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

