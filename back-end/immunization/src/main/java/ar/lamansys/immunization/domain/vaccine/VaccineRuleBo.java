package ar.lamansys.immunization.domain.vaccine;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class VaccineRuleBo {

    private Thresholds threshold;

    private Integer daysBetweenDosis;

    private VaccineSchemeBo vaccineSchemeBo;

    private VaccineDoseBo vaccineDoseBo;

    private VaccineConditionApplicationBo vaccineConditionApplicationBo;

    public VaccineRuleBo(Integer minimumDayThreshold, Integer maximumDayThreshold, Integer daysBetweenDosis,
                         VaccineSchemeBo vaccineSchemeBo,
                         VaccineDoseBo vaccineDoseBo,
                         VaccineConditionApplicationBo vaccineConditionApplicationBo) {
        this.threshold = new Thresholds(minimumDayThreshold, maximumDayThreshold);
        if (daysBetweenDosis < 0)
            throw new VaccineException(VaccineExceptionEnum.NEGATIVE_DAY_BETWEEN_DOSES, "El límite mínimo de días no puede ser negativo");
        this.daysBetweenDosis = daysBetweenDosis;
        this.vaccineSchemeBo = vaccineSchemeBo;
        this.vaccineDoseBo = vaccineDoseBo;
        this.vaccineConditionApplicationBo = vaccineConditionApplicationBo;
    }

    public boolean apply(Integer days) {
        return threshold.apply(days);
    }
}
