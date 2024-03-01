package net.pladema.procedure.infrastructure.output.repository.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.procedure.domain.EProcedureTemplateStatusBo;

@Entity
@Table(name = "procedure_template")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@EntityListeners(SGXAuditListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureTemplate extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "uuid", nullable = false, unique = true)
	private UUID uuid;

	@Column(name = "description")
	private String description;

	@Column(name = "status_id", nullable = false)
	private Short statusId;

	@PrePersist
	public void preInsert() {
		if (this.uuid == null)
			this.uuid = UUID.randomUUID();
	}

	public boolean canUpdate() {
		return (this.statusId != null) && EProcedureTemplateStatusBo.map(this.statusId).isUpdateable();
	}

	public EProcedureTemplateStatusBo toStatusBo() {
		return EProcedureTemplateStatusBo.map(this.statusId);
	}

	public static Short getStatusId(EProcedureTemplateStatusBo nextState) {
		return nextState.getId();
	}
}
