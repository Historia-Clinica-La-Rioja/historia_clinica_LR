package net.pladema.permissions.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;

@Entity
@Table(name = "permission")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Permission extends SGXAuditableEntity<Short> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5865080377750383849L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(name = "description", nullable = false)
	private String description;

}
