package net.pladema.user.application.updateuserrole;

import net.pladema.user.domain.UserRoleBo;

import java.util.List;

public interface UpdateUserRole {

    void execute(List<UserRoleBo> userRoleBos, Integer userId, Integer institutionId);
}
