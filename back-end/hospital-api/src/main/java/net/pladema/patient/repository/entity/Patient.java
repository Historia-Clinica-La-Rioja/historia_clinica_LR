package net.pladema.patient.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.repository.domain.PatientPersonVo;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=false")
@ToString
@EntityListeners(SGXAuditListener.class)
public class Patient extends SGXAuditableEntity<Integer> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7559172653664261066L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "person_id")
	private Integer personId;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	@Column(name = "possible_duplicate", nullable = false)
	private Boolean possibleDuplicate = false;

	@Column(name = "national_id")
	private Integer nationalId;

	@Column(name = "comments", length = 255)
	private String comments;

	@Column(name = "identity_verification_status_id")
	private Short identityVerificationStatusId;

	@Column(name = "to_audit", nullable = false)
	private Boolean toAudit = false;


	public Patient(PatientPersonVo patientPersonVo) {
		this.id = patientPersonVo.getId();
		this.personId = patientPersonVo.getPersonId();
		this.typeId = patientPersonVo.getPatientTypeId();
		this.possibleDuplicate = patientPersonVo.getPossibleDuplicate();
		this.nationalId = patientPersonVo.getNationalId();
		this.comments = patientPersonVo.getComments();
		this.identityVerificationStatusId = patientPersonVo.getIdentityVerificationStatusId();
	}

	public boolean isValidated() {
		return getTypeId().equals(PatientType.VALIDATED);
	}

	public Patient(Short typeId) {
		this.typeId = typeId;
	}
    
}


