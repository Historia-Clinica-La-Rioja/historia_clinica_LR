package net.pladema.nomivac.infrastructure.output.immunization.rest;

import lombok.Getter;

@Getter
public class NomivacImmunizationPostResponse {

    private final String nomivacId;

    private final String message;

    private final Integer statusCode;

    private final boolean unsuccessfullyOperation;

    public NomivacImmunizationPostResponse(String nomivacId, String message, Integer statusCode,
                                           boolean unsuccessfullyOperation) {
        this.nomivacId = nomivacId;
        this.message = message;
        this.statusCode = statusCode;
        this.unsuccessfullyOperation = unsuccessfullyOperation;
    }
}
