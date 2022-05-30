package net.pladema.clinichistory.hospitalization.service.servicerequest.exception;

import lombok.Getter;

@Getter
public class CreateInternmentServiceRequestException extends RuntimeException {

    private final CreateInternmentServiceRequestEnumException code;

    public CreateInternmentServiceRequestException(CreateInternmentServiceRequestEnumException code, String message) {
        super(message);
        this.code = code;
    }
}