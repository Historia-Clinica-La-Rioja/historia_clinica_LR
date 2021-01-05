package net.pladema.emergencycare.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "emergency_care_episode")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmergencyCareEpisode extends SGXAuditableEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 6722357862313507002L;

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "patient_medical_coverage_id")
	private Integer patientMedicalCoverageId;

	@Column(name = "emergency_care_type_id")
	private Short emergencyCareTypeId;

	@Column(name = "emergency_care_state_id")
	private Short emergencyCareStateId;

	@Column(name = "emergency_care_entrance_type_id")
	private Short emergencyCareEntranceTypeId;

	@Column(name = "triage_category_id", nullable = false)
	private Short triageCategoryId;

	@Column(name = "doctors_office_id")
	private Integer doctorsOfficeId;

	@Column(name = "ambulance_company_id", length = 15)
	private String ambulanceCompanyId;

	@Column(name = "police_intervention_id")
	private Integer policeInterventionId;
}

