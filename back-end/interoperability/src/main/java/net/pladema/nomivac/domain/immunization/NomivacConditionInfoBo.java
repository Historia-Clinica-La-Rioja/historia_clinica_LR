package net.pladema.nomivac.domain.immunization;

import lombok.Getter;

@Getter
public class NomivacConditionInfoBo {

    private final Short id;

    private final String description;

    public NomivacConditionInfoBo(Short id, String description) {
        this.id = id;
        this.description = description;
    }
}
