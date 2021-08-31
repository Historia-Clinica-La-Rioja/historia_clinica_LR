package ar.lamansys.immunization.domain.vaccine;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class VaccineBo {

    private Short id;

    private Short sisaCode;

    private VaccineDescription description;

    private Thresholds threshold;

    private List<VaccineRuleBo> rules;

    public VaccineBo(Short id, Short sisaCode, String description, Integer minimumDayThreshold, Integer maximumDayThreshold, List<VaccineRuleBo> rules) {
        this.id = id;
        this.sisaCode = sisaCode;
        this.description = new VaccineDescription(description);
        this.threshold = new Thresholds(minimumDayThreshold, maximumDayThreshold);
        this.rules = rules;
    }

    public boolean apply(Integer days) {
        return threshold.apply(days);
    }
}
