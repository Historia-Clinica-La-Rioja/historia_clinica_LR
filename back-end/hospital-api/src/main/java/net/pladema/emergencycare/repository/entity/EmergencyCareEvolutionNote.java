package net.pladema.emergencycare.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "emergency_care_evolution_note")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmergencyCareEvolutionNote extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "clinical_specialty_id")
	private Integer clinicalSpecialtyId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "doctor_id")
	private Integer doctorId;

	@Column(name = "billable", nullable = false)
	private Boolean billable;

	@Column(name = "patient_medical_coverage_id")
	private Integer patientMedicalCoverageId;

	public EmergencyCareEvolutionNote(Integer institutionId,
									  Integer patientId,
									  Integer doctorId, Integer clinicalSpecialtyId, Integer patientMedicalCoverageId) {
		super();
		this.institutionId = institutionId;
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.clinicalSpecialtyId = clinicalSpecialtyId;
		this.patientMedicalCoverageId = patientMedicalCoverageId;
		this.startDate = LocalDate.now();
		this.billable = false;
	}

}
