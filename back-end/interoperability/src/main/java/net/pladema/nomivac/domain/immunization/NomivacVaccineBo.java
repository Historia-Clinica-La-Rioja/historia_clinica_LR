package net.pladema.nomivac.domain.immunization;

import lombok.Getter;

@Getter
public class NomivacVaccineBo {

    private final String vaccineSctid;

    private final String vaccinePt;

    public NomivacVaccineBo(String vaccineSctid, String vaccinePt) {
        this.vaccineSctid = vaccineSctid;
        this.vaccinePt = vaccinePt;
    }
}
