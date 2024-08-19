package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
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

import java.io.Serializable;

@Table(name = "parameter")
@EntityListeners(SGXAuditListener.class)
@Where(clause = "deleted=false")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Parameter extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = -4793173737866786857L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "loinc_id")
	private Integer loincId;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	@Column(name = "input_count")
	private Short inputCount;

	@Column(name = "snomed_group_id")
	private Integer snomedGroupId;

	public Parameter (Integer loincId, String description, Short typeId, Short inputCount, Integer snomedGroupId) {
		this.loincId = loincId;
		this.description = description;
		this.typeId = typeId;
		this.inputCount = inputCount;
		this.snomedGroupId = snomedGroupId;
	}

}
