package ar.lamansys.refcounterref.infraestructure.output.repository.forwarding;

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

@Table(name = "reference_forwarding")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReferenceForwarding extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "reference_id", nullable = false)
	private Integer reference_id;

	@Column(name = "observation", columnDefinition = "TEXT", nullable = false)
	private String observation;

	@Column(name = "forwarding_type_id", nullable = false)
	private short forwardingTypeId;

	public ReferenceForwarding(Integer referenceId, String observation, short forwardingTypeId) {
		this.reference_id = referenceId;
		this.observation = observation;
		this.forwardingTypeId = forwardingTypeId;
	}

}
