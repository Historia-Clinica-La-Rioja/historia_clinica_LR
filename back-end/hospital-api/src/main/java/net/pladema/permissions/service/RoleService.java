package net.pladema.permissions.service;

import net.pladema.permissions.repository.enums.ERole;

public interface RoleService {

    void updateRolesStore();

    String getRoleDescription(ERole eRole);
}
