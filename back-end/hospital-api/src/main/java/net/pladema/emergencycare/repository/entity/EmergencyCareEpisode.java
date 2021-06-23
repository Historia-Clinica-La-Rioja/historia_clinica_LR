package net.pladema.emergencycare.repository.entity;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "emergency_care_episode")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmergencyCareEpisode extends SGXAuditableEntity<Integer> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6722357862313507002L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "patient_medical_coverage_id")
	private Integer patientMedicalCoverageId;

	@Column(name = "emergency_care_type_id")
	private Short emergencyCareTypeId;

	@Column(name = "emergency_care_state_id", nullable = false)
	private Short emergencyCareStateId;

	@Column(name = "emergency_care_entrance_type_id")
	private Short emergencyCareEntranceTypeId;

	@Column(name = "triage_category_id", nullable = false)
	private Short triageCategoryId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "doctors_office_id")
	private Integer doctorsOfficeId;

	@Column(name = "ambulance_company_id", length = 15)
	private String ambulanceCompanyId;

	@Column(name = "has_police_intervention")
	private Boolean hasPoliceIntervention;

	public EmergencyCareEpisode(EmergencyCareBo emergencyCareBo,
								TriageBo triageBo) {
		this.id = emergencyCareBo.getId();
		this.patientId = emergencyCareBo.getPatient() != null ? emergencyCareBo.getPatient().getId() : null;
		this.patientMedicalCoverageId = emergencyCareBo.getPatient() != null ? emergencyCareBo.getPatient().getPatientMedicalCoverageId() : null;
		this.emergencyCareTypeId = emergencyCareBo.getEmergencyCareTypeId();
		this.emergencyCareStateId = emergencyCareBo.getEmergencyCareStateId();
		this.emergencyCareEntranceTypeId = emergencyCareBo.getEmergencyCareEntranceId();
		this.triageCategoryId = triageBo.getCategoryId();
		this.doctorsOfficeId = emergencyCareBo.getDoctorsOfficeId();
		this.institutionId = emergencyCareBo.getInstitutionId();
		this.ambulanceCompanyId = emergencyCareBo.getAmbulanceCompanyId();
		this.hasPoliceIntervention = emergencyCareBo.getHasPoliceIntervention();
	}

	@PrePersist
	void preInsert() {
		if (this.emergencyCareStateId == null) {
			this.emergencyCareStateId = EmergencyCareState.EN_ESPERA;
		}
	}
}

