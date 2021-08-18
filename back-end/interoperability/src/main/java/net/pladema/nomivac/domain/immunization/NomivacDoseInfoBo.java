package net.pladema.nomivac.domain.immunization;

import lombok.Getter;

@Getter
public class NomivacDoseInfoBo {

    private final String dose;

    private final Short doseOrder;

    public NomivacDoseInfoBo(String dose, Short doseOrder) {
        this.dose = dose;
        this.doseOrder = doseOrder;
    }
}
