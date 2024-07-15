package net.pladema.parameter.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Table(name = "parameter_text_option")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@Where(clause = "deleted=false")
@Entity
public class ParameterTextOption extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = -396721242325515948L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "parameter_id", nullable = false)
	private Integer parameterId;
}
