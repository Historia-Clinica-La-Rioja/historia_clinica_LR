package ar.lamansys.sgh.shared.infrastructure.output.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "health_insurance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SharedHealthInsurance extends SharedMedicalCoverage {

	@Column(name = "rnos", nullable = false)
	private Integer rnos;

	@Column(name = "acronym", length = 18)
	private String acronym;

}
