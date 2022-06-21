package net.pladema.patient.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "private_health_insurance")
@Getter
@Setter
@NoArgsConstructor
public class PrivateHealthInsurance extends MedicalCoverage {

    public PrivateHealthInsurance(Integer id, String name, String cuit, Short type){
        setId(id);
        setName(name);
        setCuit(cuit);
		setType(type);
    }

}
