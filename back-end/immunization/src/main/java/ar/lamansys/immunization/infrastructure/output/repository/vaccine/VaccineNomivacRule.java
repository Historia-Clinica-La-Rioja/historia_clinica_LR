package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "vaccine_nomivac_rule")
@NoArgsConstructor
public class VaccineNomivacRule {

    @EmbeddedId
    private VaccineNomivacRulePK pk;

    @Setter
    @Column(name = "minimum_threshold_days", nullable = false)
    private Integer minimumThresholdDays;

    @Setter
    @Column(name = "maximum_threshold_days", nullable = false)
    private Integer maximumThresholdDays;

    @Setter
    @Column(name = "time_between_doses_days", nullable = false)
    private Integer timeBetweenDosesDays;

    public VaccineNomivacRule(Short sisaCode, Short conditionApplicationId, Short schemeId, String dose, Short doseOrder,
                              Integer minimumThresholdDays, Integer maximumThresholdDays, Integer timeBetweenDosesDays) {
        this.pk = new VaccineNomivacRulePK(sisaCode, conditionApplicationId, schemeId, dose, doseOrder);
        this.minimumThresholdDays = minimumThresholdDays;
        this.maximumThresholdDays = maximumThresholdDays;
        this.timeBetweenDosesDays = timeBetweenDosesDays;
    }
}
