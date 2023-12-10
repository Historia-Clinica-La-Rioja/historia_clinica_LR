package net.pladema.procedure.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "procedure_template")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@EntityListeners(SGXAuditListener.class)
public class ProcedureTemplate extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "uuid", nullable = false, unique = true)
	private UUID uuid;

	@Column(name = "description")
	private String description;

	@PrePersist
	public void preInsert() {
		if (this.uuid == null)
			this.uuid = UUID.randomUUID();
	}

}
