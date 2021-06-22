package ar.lamansys.immunization.domain.vaccine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VaccineSchemeBo {

    private Short id;

    private String description;

    private Thresholds threshold;

    public VaccineSchemeBo(Short id, String description, Integer minimumDayThreshold, Integer maximumDayThreshold) {
        this.id = id;
        this.description = description;
        this.threshold = new Thresholds(minimumDayThreshold, maximumDayThreshold);
    }

    public boolean apply(Integer days) {
        return threshold.apply(days);
    }
}
