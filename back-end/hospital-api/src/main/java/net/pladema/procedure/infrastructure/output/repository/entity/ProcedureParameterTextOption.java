package net.pladema.procedure.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@Table(name = "procedure_parameter_text_option")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@EntityListeners(SGXAuditListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureParameterTextOption extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

	@Column(name = "procedure_parameter_id", nullable = false)
	private Integer procedureParameterId;

}
