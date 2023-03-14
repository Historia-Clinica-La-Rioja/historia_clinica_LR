package net.pladema.permissions.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.Assert;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.repository.enums.ERoleLevel;

@Entity
@Table(name = "user_role")
@EntityListeners(SGXAuditListener.class)
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class UserRole extends SGXAuditableEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2826874188803670205L;

	private static final String ROLE_REQUIRE_INSTITUTION = "role-level.institution.required";
	private static final String BAD_ROLE_LEVEL = "role-level.wrong_level";

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "role_id", nullable = false)
	private Short roleId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;
	public UserRole(Integer userId, Short roleId) {
		this(userId, roleId, -1);
	}
	
	public UserRole(Integer userId, Short roleId, Integer institutionId) {
		this.userId = userId;
		this.roleId = roleId;
		this.institutionId = institutionId;
		if (ERole.map(roleId).getLevel() == ERoleLevel.LEVEL0) {
			this.institutionId = -1;
		} else {
			Assert.notNull(institutionId, ROLE_REQUIRE_INSTITUTION);
			this.institutionId = institutionId;
		}
		setDeleted(false);
	}


}
