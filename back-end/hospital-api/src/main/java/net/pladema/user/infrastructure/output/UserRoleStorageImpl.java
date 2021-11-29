package net.pladema.user.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.service.RoleService;
import net.pladema.user.application.port.UserRoleStorage;
import net.pladema.user.domain.UserRoleBo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleStorageImpl implements UserRoleStorage {

    private final UserRoleRepository userRoleRepository;

    private final RoleService roleService;

    @Override
    public List<UserRoleBo> getRolesByUser(Integer userId) {
        List<UserRoleBo> result = new ArrayList<>();
        userRoleRepository.getRoleAssignments(userId)
                .forEach(r ->
                        result.add(new UserRoleBo(
                                r.getRole().getId(),
                                roleService.getRoleDescription(r.getRole()),
                                userId,
                                r.institutionId)));
        return result;
    }
}
