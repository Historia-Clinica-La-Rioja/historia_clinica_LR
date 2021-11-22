package net.pladema.nomivac.domain.immunization;

import lombok.Getter;

@Getter
public class NomivacSchemeInfoBo {

    private final Short id;

    private final String description;

    public NomivacSchemeInfoBo(Short id, String description) {
        this.id = id;
        this.description = description;
    }
}
