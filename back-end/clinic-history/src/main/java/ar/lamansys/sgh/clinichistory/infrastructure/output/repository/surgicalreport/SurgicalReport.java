package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.surgicalreport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "surgical_report")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SurgicalReport extends SGXAuditableEntity<Integer> {

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

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "doctor_id")
	private Integer doctorId;

	@Column(name = "billable", nullable = false)
	private Boolean billable;

	@Column(name = "patient_medical_coverage_id")
	private Integer patientMedicalCoverageId;

	@Column(name = "start_date_time")
	private LocalDateTime startDateTime;

	@Column(name = "end_date_time")
	private LocalDateTime endDateTime;

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "has_prosthesis")
	private Boolean hasProsthesis;

}
