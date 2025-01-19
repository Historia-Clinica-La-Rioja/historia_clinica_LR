package net.pladema.establishment.repository.entity;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Table(name = "institutional_parameterized_form")
@Entity
public class InstitutionalParameterizedForm extends SGXAuditableEntity<Integer> {

	private static final long serialVersionUID = 3223605345301431308L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "parameterized_form_id", nullable = false)
	private Integer parameterizedFormId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "is_enabled", nullable = false)
	private Boolean isEnabled;

	public InstitutionalParameterizedForm(Integer parameterizedFormId, Integer institutionId, Boolean isEnabled) {
		this.parameterizedFormId = parameterizedFormId;
		this.institutionId = institutionId;
		this.isEnabled = isEnabled;
	}

}
