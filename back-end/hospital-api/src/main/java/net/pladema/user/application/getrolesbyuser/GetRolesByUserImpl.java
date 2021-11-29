package net.pladema.user.application.getrolesbyuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.user.application.port.UserRoleStorage;
import net.pladema.user.domain.UserRoleBo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetRolesByUserImpl implements GetRolesByUser {

    private final UserRoleStorage userRoleStorage;

    @Override
    public List<UserRoleBo> execute(Integer userId, Integer institutionId) {
        log.debug("Input parameter -> userId {}, institutionId {}", userId, institutionId);
        List<UserRoleBo> result = userRoleStorage.getRolesByUser(userId)
                .stream().filter(userRoleBo -> userRoleBo.getInstitutionId().equals(institutionId))
                .collect(Collectors.toList());
        log.debug("Output ->{}", result);
        return result;
    }
}
