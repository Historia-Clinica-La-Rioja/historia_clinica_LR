package net.pladema.patient.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "private_health_insurance")
@Getter
@Setter
@NoArgsConstructor
public class PrivateHealthInsurance extends MedicalCoverage {

    @Column(name = "plan", length = 10)
    private String plan;

    public PrivateHealthInsurance(Integer id, String name, String plan){
        setId(id);
        setName(name);
        this.plan=plan;
    }

}
