package net.pladema.nomivac.domain.immunization;

import lombok.Getter;

@Getter
public class NomivacInstitutionBo {

    private String name;

    private String sisaCode;

    public NomivacInstitutionBo(String name, String sisaCode) {
        this.name = name;
        this.sisaCode = sisaCode;
    }
}
