package ar.lamansys.virtualConsultation.infrastructure.output.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "virtual_consultation")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
public class VirtualConsultation extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "attendant_healthcare_professional_id")
	private Integer attendantHealthcareProfessionalId;

	@Column(name = "clinical_specialty_id", nullable = false)
	private Integer clinicalSpecialtyId;

	@Column(name = "care_line_id", nullable = false)
	private Integer careLineId;

	@Column(name = "problem_id")
	private Integer problemId;

	@Column(name = "priority_id", nullable = false)
	private Short priorityId;

	@Column(name = "motive_id", nullable = false)
	private Integer motiveId;

	@Column(name = "responsible_healthcare_professional_id", nullable = false)
	private Integer responsibleHealthcareProfessionalId;

	@Column(name = "status_id", nullable = false)
	private Short statusId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "call_id", nullable = false, length = 36)
	private String callId;

}
