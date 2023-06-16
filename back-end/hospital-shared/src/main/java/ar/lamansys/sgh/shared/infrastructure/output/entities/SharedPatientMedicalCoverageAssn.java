package ar.lamansys.sgh.shared.infrastructure.output.entities;

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

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "patient_medical_coverage")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SharedPatientMedicalCoverageAssn implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "medical_coverage_id", nullable = false)
	private Integer medicalCoverageId;

	@Column(name = "active", nullable = false)
	private Boolean active;

	@Column(name = "vigency_date")
	private LocalDate vigencyDate;

	@Column( name = "affiliate_number", length = 25)
	private String affiliateNumber;

	@Column(name = "private_health_insurance_details_id")
	private Integer privateHealthInsuranceDetailsId;

	@Column(name = "condition_id")
	private Short conditionId;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "plan_id")
	private Integer planId;

}
