package net.pladema.nomivac.infrastructure.output.immunization.rest;

import lombok.Getter;

@Getter
public class NomivacImmunizationPostResponse {

    private final String nomivacId;

    private final String message;

    private final Integer statusCode;

    public NomivacImmunizationPostResponse(String nomivacId, String message, Integer statusCode) {
        this.nomivacId = nomivacId;
        this.message = message;
        this.statusCode = statusCode;
    }
}
