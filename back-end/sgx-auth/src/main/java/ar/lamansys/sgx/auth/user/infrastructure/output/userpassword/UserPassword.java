package ar.lamansys.sgx.auth.user.infrastructure.output.userpassword;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "user_password")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
public class UserPassword extends SGXAuditableEntity<Integer> {

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

}
