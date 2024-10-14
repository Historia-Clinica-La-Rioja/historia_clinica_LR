package net.pladema.medicine.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(SGXAuditListener.class)
@Table(name = "medicine_group")
@Entity
@Where(clause = "deleted = false")
public class MedicineGroup extends SGXAuditableEntity<Integer>  {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "requires_audit", nullable = false)
	private Boolean requiresAudit;

	@Column(name = "outpatient", nullable = false)
	private Boolean outpatient;

	@Column(name = "internment", nullable = false)
	private Boolean internment;

	@Column(name = "emergency_care", nullable = false)
	private Boolean emergencyCare;

	@Column(name = "message")
	private String message;

	@Column(name = "all_diagnoses", nullable = false)
	private Boolean allDiagnoses;

	@Column(name = "is_domain", nullable = false)
	private Boolean isDomain;

	@Column(name = "required_documentation", columnDefinition = "TEXT")
	private String requiredDocumentation;

}
