package net.pladema.unitofmeasure.insfrastructure.output.entity;

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
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "unit_of_measure")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UnitOfMeasure extends SGXAuditableEntity<Short> implements Serializable {
	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "enabled", nullable = false)
	private Boolean enabled;

	public boolean updateAllowed(UnitOfMeasure original) {
		return this.id.equals(original.id) && this.code.equals(original.code)
		&& this.description.equals(original.description);
	}
}
