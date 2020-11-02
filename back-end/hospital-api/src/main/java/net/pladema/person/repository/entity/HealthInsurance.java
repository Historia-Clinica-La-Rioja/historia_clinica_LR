package net.pladema.person.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.repository.entity.MedicalCoverage;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "health_insurance")
@Getter
@Setter
@NoArgsConstructor
public class HealthInsurance extends MedicalCoverage {
    /*
     */
    private static final long serialVersionUID = 2873716268832417941L;

    @Column(name = "rnos", nullable = false)
    private Integer rnos;

    @Column(name = "acronym", length = 18)
    private String acronym;

    public HealthInsurance(Integer id, String name, Integer rnos, String acronym){
        setId(id);
        setName(name);
        this.rnos = rnos;
        this.acronym = acronym;
    }
}
