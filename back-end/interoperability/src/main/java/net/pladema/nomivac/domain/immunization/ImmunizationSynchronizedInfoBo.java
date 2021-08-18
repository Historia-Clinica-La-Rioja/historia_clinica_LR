package net.pladema.nomivac.domain.immunization;

import lombok.Getter;

@Getter
public class ImmunizationSynchronizedInfoBo {

    private Integer immunizationId;

    private String externalId;

    private Integer statusCode;

    public ImmunizationSynchronizedInfoBo(Integer immunizationId, String externalId, Integer statusCode) {
        this.immunizationId = immunizationId;
        this.externalId = externalId;
        this.statusCode = statusCode;
    }

}
