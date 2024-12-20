package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository.entity;

import lombok.AllArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medication_request_validation_medical_coverage")
@Entity
public class MedicationRequestValidationMedicalCoverage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Short id;

	@Column(name = "funder_number")
	private Short funderNumber;

	@Column(name = "cuit", length = 20)
	private String cuit;

	@Column(name = "commercial_name")
	private String commercialName;

}
