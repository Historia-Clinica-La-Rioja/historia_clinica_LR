package net.pladema.user.application.port;

import net.pladema.user.domain.PersonDataBo;
import net.pladema.user.domain.UserRoleBo;

import java.util.List;

public interface UserRoleStorage {

    List<UserRoleBo> getRolesByUser(Integer userId);

    void updateUserRole(List<UserRoleBo> userRolesBo, Integer userId, Integer institutionId);

	boolean hasRoleInInstitution(Integer userId, Integer institutionId);

	List<PersonDataBo> getUsersByRoles(Integer institutionId, List<Short> rolesId);
}
