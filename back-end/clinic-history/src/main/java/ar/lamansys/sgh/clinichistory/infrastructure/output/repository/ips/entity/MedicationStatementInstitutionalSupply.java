package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Setter
@Getter
@EntityListeners(SGXAuditListener.class)
@Table(name = "medication_statement_institutional_supply")
@Entity
public class MedicationStatementInstitutionalSupply extends SGXAuditableEntity<Integer> {

	private static final long serialVersionUID = 3802489021005656646L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "medication_statement_id", nullable = false)
	private Integer medicationStatementId;

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Column(name = "quantity", nullable = false)
	private Short quantity;

	@Column(name = "institution_id")
	private Integer institutionId;

}
