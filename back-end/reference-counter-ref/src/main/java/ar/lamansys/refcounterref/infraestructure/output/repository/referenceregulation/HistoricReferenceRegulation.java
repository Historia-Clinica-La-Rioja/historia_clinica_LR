package ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
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

@Entity
@Table(name = "historic_reference_regulation")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HistoricReferenceRegulation extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "reference_id", nullable = false)
	private Integer referenceId;

	@Column(name = "rule_id")
	private Integer ruleId;

	@Column(name = "rule_level")
	private Short ruleLevel;

	@Column(name = "state_id", nullable = false)
	private Short stateId;

	@Column(name = "reason")
	private String reason;

	public HistoricReferenceRegulation(Integer referenceId, Short stateId) {
		this.referenceId = referenceId;
		this.stateId = stateId;
	}
}
