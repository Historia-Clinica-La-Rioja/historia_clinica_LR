package net.pladema.user.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "v_hospital_users")
@Immutable
@Setter
@Getter
@ToString
public class VHospitalUser {

	@Id
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "username", nullable = false, unique = true, length = 100)
	private String username;

	@Column(name = "enable")
	private Boolean enable = false;

	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@Column(name = "person_id")
	private Integer personId;

	@Column(name = "two_factor_authentication_enabled")
	private Boolean twoFactorAuthenticationEnabled = false;

}
