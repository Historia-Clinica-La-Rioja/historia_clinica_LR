package ar.lamansys.refcounterref.infraestructure.output.repository.referenceobservation;

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

@Table(name = "reference_observation")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReferenceObservation extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "reference_id")
	private Integer reference_id;

	@Column(name = "observation", columnDefinition = "TEXT")
	private String observation;

	public ReferenceObservation(Integer referenceId, String observation) {
		this.reference_id = referenceId;
		this.observation = observation;
	}
}
