package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "vaccine_scheme")
@NoArgsConstructor
public class VaccineScheme {

    @Id
    @Column(name = "id")
    private Short id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "minimum_threshold_days", nullable = false)
    private Integer minimumThresholdDays;

    @Column(name = "maximum_threshold_days", nullable = false)
    private Integer maximumThresholdDays;

    public VaccineScheme(Short id, String description, Integer minimumThresholdDays, Integer maximumThresholdDays) {
        this.id = id;
        this.description = description;
        this.minimumThresholdDays = minimumThresholdDays;
        this.maximumThresholdDays = maximumThresholdDays;
    }
}
