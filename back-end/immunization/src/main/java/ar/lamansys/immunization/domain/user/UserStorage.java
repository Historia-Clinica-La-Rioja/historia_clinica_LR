package ar.lamansys.immunization.domain.user;

import java.util.List;

public interface UserStorage {

    List<RoleInfoBo> fetchLoggedUserRoles();
}
