package net.pladema.patient.repository.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "patient_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientHistory implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5191171085707681889L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	@Column(name = "national_id")
	private Integer nationalId;

	@Column(name = "audit_type_id", nullable = false)
	private Short auditTypeId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	public PatientHistory(Patient p){
		this.patientId = p.getId();
		this.typeId = p.getTypeId();
		this.auditTypeId = p.getAuditTypeId();
		this.nationalId = p.getNationalId();
		this.createdOn = LocalDateTime.now();
		this.createdBy = SecurityContextUtils.getUserDetails().userId;
	}
    
}


