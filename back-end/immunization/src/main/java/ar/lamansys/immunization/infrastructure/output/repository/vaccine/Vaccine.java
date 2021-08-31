package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "vaccine")
@NoArgsConstructor
public class Vaccine {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "minimum_threshold_days", nullable = false)
    private Integer minimumThresholdDays;

    @Column(name = "maximum_threshold_days", nullable = false)
    private Integer maximumThresholdDays;

    @Column(name = "sisa_code")
    private Short sisaCode;

    public Vaccine(Short sisaCode, String description, Integer minimumThresholdDays, Integer maximumThresholdDays) {
        this.sisaCode = sisaCode;
        this.description = description;
        this.minimumThresholdDays = minimumThresholdDays;
        this.maximumThresholdDays = maximumThresholdDays;
    }
}
