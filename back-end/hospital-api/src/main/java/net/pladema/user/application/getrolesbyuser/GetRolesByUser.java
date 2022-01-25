package net.pladema.user.application.getrolesbyuser;

import net.pladema.user.domain.UserRoleBo;

import java.util.List;

public interface GetRolesByUser {
    List<UserRoleBo> execute(Integer userId, Integer institutionId);
}
