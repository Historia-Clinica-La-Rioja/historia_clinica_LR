package net.pladema.permissions.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.repository.enums.ERoleLevel;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
/**
 * Para el caso de los roles donde ERole.isAdmin == true, la institución es -1.
 * Esos son roles de administración que no están asociados a ninguna institución.
 */
public class UserRolePK implements Serializable {

	private static final long serialVersionUID = -5482098137325590681L;

	private static final String ROLE_REQUIRE_INSTITUTION = "role-level.institution.required";
	private static final String BAD_ROLE_LEVEL = "role-level.wrong_level";
	public static final Number UNDEFINED_ID = -1;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "role_id", nullable = false)
	private Short roleId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	public UserRolePK(Integer userId, Short roleId) {
		this(userId, roleId, -1);
	}

	public UserRolePK(Integer userId, Short roleId, Integer institutionId) {
		this.userId = userId;
		this.roleId = roleId;
		if (ERole.map(roleId).getLevel() == ERoleLevel.LEVEL0) {
			this.institutionId = -1;
		} else {
			Assert.notNull(institutionId, ROLE_REQUIRE_INSTITUTION);
			this.institutionId = institutionId;
		}
	}
}
