package net.pladema.user.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.sgx.auditable.entity.AuditableEntity;
import net.pladema.sgx.auditable.listener.AuditListener;

@Entity
@Table(name = "user_password")
@EntityListeners(AuditListener.class)
@Getter
@Setter
@NoArgsConstructor
public class UserPassword extends AuditableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7104748562864239397L;

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "salt", nullable = false)
	private String salt;

	@Column(name = "hash_algorithm", nullable = false)
	private String hashAlgorithm;

	public UserPassword(User user) {
		super();
		this.id = user.getId();
	}

}
