package net.pladema.permissions.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.auditable.entity.AuditableEntity;
import net.pladema.auditable.listener.AuditListener;

@Entity
@Table(name = "permission")
@EntityListeners(AuditListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Permission extends AuditableEntity {

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
