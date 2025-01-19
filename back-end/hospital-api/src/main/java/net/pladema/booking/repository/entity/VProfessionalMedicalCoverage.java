package net.pladema.booking.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.ToString;

import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "v_healthcare_professional_health_insurance")
@Immutable
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VProfessionalMedicalCoverage {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "healthcare_professional_id")
	private Integer healthcareProfessionalId;

	@Column(name = "medical_coverage_id")
	private Integer medicalCoverageId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	public VProfessionalMedicalCoverage(Integer id, Integer healthcareProfessionalId, Integer medicalCoverageId) {
		this.id = id;
		this.healthcareProfessionalId = healthcareProfessionalId;
		this.medicalCoverageId = medicalCoverageId;
	}

}