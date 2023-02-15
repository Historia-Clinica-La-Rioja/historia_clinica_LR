package ar.lamansys.sgh.shared.infrastructure.output.entities;

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
public class SharedPrivateHealthInsurance extends SharedMedicalCoverage {
}
