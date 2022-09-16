package ar.lamansys.sgx.auth.user.infrastructure.output.user;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
public class User extends SGXAuditableEntity<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "username", nullable = false, unique = true, length = 100)
	private String username;
	
	@Column(name = "enable", nullable = false)
	private Boolean enable = false;

	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@Column(name = "previous_login")
	private LocalDateTime previousLogin;
	
	@Column(name = "two_factor_authentication_secret", length = 64)
	private String twoFactorAuthenticationSecret;

	@Column(name = "two_factor_authentication_enabled", nullable = false)
	private Boolean twoFactorAuthenticationEnabled = false;

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", enable=" + enable +
				", lastLogin=" + lastLogin +
				", previousLogin=" + previousLogin +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}

}
