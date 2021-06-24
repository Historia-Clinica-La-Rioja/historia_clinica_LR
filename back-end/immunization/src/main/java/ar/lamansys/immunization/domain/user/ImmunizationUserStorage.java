package ar.lamansys.immunization.domain.user;

import java.util.List;

public interface ImmunizationUserStorage {

    List<RoleInfoBo> fetchLoggedUserRoles();
}
