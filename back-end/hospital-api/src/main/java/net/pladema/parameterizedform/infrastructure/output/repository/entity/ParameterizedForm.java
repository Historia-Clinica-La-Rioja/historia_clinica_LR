package net.pladema.parameterizedform.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Table(name = "parameterized_form")
@Entity
public class ParameterizedForm extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = 167980588094284627L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "status_id", nullable = false)
	private Short statusId;

	@Column(name = "outpatient_enabled", nullable = false)
	private Boolean outpatientEnabled;

	@Column(name = "internment_enabled", nullable = false)
	private Boolean internmentEnabled;

	@Column(name = "emergency_care_enabled", nullable = false)
	private Boolean emergencyCareEnabled;

}
