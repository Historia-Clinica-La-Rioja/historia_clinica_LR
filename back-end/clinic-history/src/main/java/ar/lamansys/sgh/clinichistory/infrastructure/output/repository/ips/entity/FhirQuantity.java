package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fhir_quantity")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FhirQuantity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "value", nullable = false)
	private Float value;

	@Column(name = "unit", length = 20, nullable = false)
	private String unit;

}
