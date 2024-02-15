package net.pladema.procedure.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

@Entity
@Table(name = "procedure_parameter")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@EntityListeners(SGXAuditListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted=false")
public class ProcedureParameter extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "procedure_template_id", nullable = false)
	private Integer procedureTemplateId;

	@Column(name = "loinc_id", nullable = false)
	private Integer loincId;

	@Column(name = "order_number", nullable = false)
	private Short orderNumber;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	@Column(name = "input_count")
	private Short inputCount;

	@Column(name = "snomed_group_id")
	private Integer snomedGroupId;

	/**
	 * Sets appropriate field values
	 * to store when deleted
	 */
	public void delete() {
		this.orderNumber = (short) -1;
	}
}
