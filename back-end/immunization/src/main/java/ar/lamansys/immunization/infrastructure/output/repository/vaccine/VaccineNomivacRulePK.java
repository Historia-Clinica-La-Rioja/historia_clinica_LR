package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
public class VaccineNomivacRulePK implements Serializable {

    @Column(name = "sisa_code", nullable = false)
    @EqualsAndHashCode.Include
    private Short sisaCode;

    @Column(name = "condition_application_id", nullable = false)
    @EqualsAndHashCode.Include
    private Short conditionApplicationId;

    @Column(name = "scheme_id", nullable = false)
    @EqualsAndHashCode.Include
    private Short schemeId;

    @Column(name = "dose", nullable = false)
    @EqualsAndHashCode.Include
    private String dose;

    @Column(name = "dose_order", nullable = false)
    @EqualsAndHashCode.Include
    private Short doseOrder;

    public VaccineNomivacRulePK(Short sisaCode, Short conditionApplicationId, Short schemeId, String dose, Short doseOrder) {
        this.sisaCode = sisaCode;
        this.conditionApplicationId = conditionApplicationId;
        this.schemeId = schemeId;
        this.dose = dose;
        this.doseOrder = doseOrder;
    }
}
