package net.pladema.hl7.supporting.conformance.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FhirClientException extends Exception {

    private final FhirClientEnumException code;

    private final HttpStatus status;

    public FhirClientException(FhirClientEnumException code, HttpStatus httpStatus, String responseBody) {
        super(responseBody);
        this.status = httpStatus;
        this.code = code;
    }
}
