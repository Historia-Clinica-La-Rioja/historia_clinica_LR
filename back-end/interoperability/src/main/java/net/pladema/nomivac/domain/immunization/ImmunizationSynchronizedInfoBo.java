package net.pladema.nomivac.domain.immunization;

import lombok.Getter;

@Getter
public class ImmunizationSynchronizedInfoBo {

    private Integer immunizationId;

    private String externalId;

    private Integer statusCode;

    private String message;

    private boolean unsuccessfullyOperation;

    public ImmunizationSynchronizedInfoBo(Integer immunizationId, String externalId, Integer statusCode,
                                          String message, boolean unsuccessfullyOperation) {
        this.immunizationId = immunizationId;
        this.externalId = externalId;
        this.statusCode = statusCode;
        this.message = message;
        this.unsuccessfullyOperation = unsuccessfullyOperation;
    }

}
