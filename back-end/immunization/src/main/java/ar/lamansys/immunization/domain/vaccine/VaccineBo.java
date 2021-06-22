package ar.lamansys.immunization.domain.vaccine;

import lombok.Getter;

import java.util.List;

@Getter
public class VaccineBo {

    private Short id;

    private VaccineDescription description;

    private Thresholds threshold;

    private List<VaccineRuleBo> rules;

    public VaccineBo(Short id, String description, Integer minimumDayThreshold, Integer maximumDayThreshold, List<VaccineRuleBo> rules) {
        this.id = id;
        this.description = new VaccineDescription(description);
        this.threshold = new Thresholds(minimumDayThreshold, maximumDayThreshold);
        this.rules = rules;
    }

    public boolean apply(Integer days) {
        return threshold.apply(days);
    }
}
