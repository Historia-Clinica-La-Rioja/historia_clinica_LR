package net.pladema.permissions.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgx.shared.auditable.entity.AuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.AuditListener;

@Entity
@Table(name = "role")
@EntityListeners(AuditListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AuditableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8559874558695520176L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(name = "description", nullable = false)
	private String description;

}
