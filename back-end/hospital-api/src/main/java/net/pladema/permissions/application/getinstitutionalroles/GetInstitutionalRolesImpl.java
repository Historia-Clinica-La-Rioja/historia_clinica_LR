package net.pladema.permissions.application.getinstitutionalroles;

import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.RoleUtils;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.repository.enums.ERoleLevel;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.RoleService;
import net.pladema.permissions.service.domain.RoleBo;
import net.pladema.permissions.service.dto.RoleAssignment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class GetInstitutionalRolesImpl implements GetInstitutionalRoles {

    private final Supplier<List<String>> loggedUserClaims;

    private final RoleService roleService;

    public GetInstitutionalRolesImpl(LoggedUserService loggedUserService,
                                     RoleService roleService) {
        this.loggedUserClaims = () -> toRoleName(loggedUserService.currentAssignments());
        this.roleService = roleService;
    }

    public List<RoleBo> execute() {
        log.debug("No input parameters");
        List<RoleBo> result = new ArrayList<>();
        Arrays.stream(ERole.values())
                .filter(e -> e.getLevel().equals(ERoleLevel.LEVEL1))
                .forEach(eRole -> {
                    if (RoleUtils.loggedUserHasHigherRank(loggedUserClaims.get(), List.of(eRole.getValue()))) {
                        result.add(new RoleBo(eRole.getId(), roleService.getRoleDescription(eRole)));
                    }
                });
        log.debug("Output -> {}", result);
        return result;
    }

    private static List<String> toRoleName(Stream<RoleAssignment> assignments) {
        return assignments
                .map(assignment -> assignment.role.getValue())
                .collect(Collectors.toList());
    }
}
