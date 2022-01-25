package net.pladema.user.application.updateuserrole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.user.application.port.UserRoleStorage;
import net.pladema.user.domain.UserRoleBo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateUserRoleImpl implements UpdateUserRole {

    private final UserRoleStorage userRoleStorage;

    @Override
    public void execute(List<UserRoleBo> userRoleBos, Integer userId, Integer institutionId) {
        log.debug("Input parameters -> {}", userRoleBos);
        userRoleStorage.updateUserRole(userRoleBos, userId,institutionId);
    }
}
