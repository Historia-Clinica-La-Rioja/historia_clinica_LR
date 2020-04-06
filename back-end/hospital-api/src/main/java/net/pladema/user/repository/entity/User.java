package net.pladema.user.repository.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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
@Table(name = "users")
@EntityListeners(AuditListener.class)
@Getter
@Setter
@NoArgsConstructor
public class User extends AuditableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "username", nullable = false, unique = true)
	private String username;
	
	@Column(name = "enable", nullable = false)
	private Boolean enable = false;

	@Column(name = "person_id")
	private Integer personId;
	
	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", enable=" + enable +
				", personId=" + personId +
				", lastLogin=" + lastLogin +
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
