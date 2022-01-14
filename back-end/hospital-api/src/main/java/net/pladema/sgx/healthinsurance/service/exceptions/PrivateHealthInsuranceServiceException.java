package net.pladema.sgx.healthinsurance.service.exceptions;

import lombok.Getter;

@Getter
public class PrivateHealthInsuranceServiceException extends RuntimeException {

    private final PrivateHealthInsuranceServiceEnumException code;

    public PrivateHealthInsuranceServiceException(PrivateHealthInsuranceServiceEnumException code, String message) {
        super(message);
        this.code = code;
    }
}