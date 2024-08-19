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
@Table(name = "fhir_observation")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FhirObservation {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "fhir_observation_group_id", nullable = false)
	private Integer fhirObservationGroupId;

	@Column(name = "loinc_code", length = 20, nullable = false)
	private String loincCode;

	@Column(name = "value", length = 20)
	private String value;

	@Column(name = "quantity_id")
	private Integer quantityId;
}
