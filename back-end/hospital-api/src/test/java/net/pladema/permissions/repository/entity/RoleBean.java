package net.pladema.permissions.repository.entity;

import net.pladema.permissions.repository.enums.ERole;

public class RoleBean {
    private RoleBean() {
        
    }

    public static Role newRole(ERole eRole) {
        Role role = new Role();
        role.setDescription(eRole.getValue());
        return role;
    }
}