package net.pladema.snvs.domain.institution;

import lombok.Getter;

@Getter
public class InstitutionDataBo {

    private final Integer id;

    private final String sisaCode;

    public InstitutionDataBo(Integer id, String sisaCode) {
        this.id = id;
        this.sisaCode = sisaCode;
    }
}
