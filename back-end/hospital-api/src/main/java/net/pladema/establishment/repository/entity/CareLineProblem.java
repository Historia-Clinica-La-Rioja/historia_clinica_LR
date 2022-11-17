package net.pladema.establishment.repository.entity;

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

import java.io.Serializable;

@Entity
@Table(name = "care_line_problem")
@Getter
@Setter
@ToString
@EntityListeners(SGXAuditListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class CareLineProblem extends SGXAuditableEntity<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "care_line_id", nullable = false)
	private Integer careLineId;

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Override
	public Integer getId() {
		return this.id;
	}

}
