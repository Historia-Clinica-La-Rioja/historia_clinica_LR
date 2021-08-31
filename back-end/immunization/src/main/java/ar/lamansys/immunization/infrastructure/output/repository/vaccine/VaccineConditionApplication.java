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
@Table(name = "vaccine_condition_application")
@NoArgsConstructor
public class VaccineConditionApplication {

    @Id
    @Column(name = "id")
    private Short id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    public VaccineConditionApplication(Short id, String description) {
        this.id = id;
        this.description = description;
    }
}
