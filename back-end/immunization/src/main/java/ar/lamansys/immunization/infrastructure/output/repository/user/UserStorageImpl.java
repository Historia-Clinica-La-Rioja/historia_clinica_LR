package ar.lamansys.immunization.infrastructure.output.repository.user;

import ar.lamansys.immunization.domain.user.RoleInfoBo;
import ar.lamansys.immunization.domain.user.UserStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.RoleInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPermissionPort;
import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserStorageImpl implements UserStorage {

    private final SharedPermissionPort sharedPermissionPort;

    public UserStorageImpl(SharedPermissionPort sharedPermissionPort) {
        this.sharedPermissionPort = sharedPermissionPort;
    }

    @Override
    public List<RoleInfoBo> fetchLoggedUserRoles() {
        return sharedPermissionPort.ferPermissionInfoByUserId(SecurityContextUtils.getUserDetails().userId)
                .stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    private RoleInfoBo mapTo(RoleInfoDto roleInfoDto) {
        return new RoleInfoBo(roleInfoDto.getId(), roleInfoDto.getInstitution(), roleInfoDto.getValue());
    }
}
