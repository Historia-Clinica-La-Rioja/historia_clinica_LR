package net.pladema.permissions.application.getprofessionalroles;

import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.RoleService;
import net.pladema.permissions.service.domain.RoleBo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class GetProfessionalRolesImpl implements GetProfessionalRoles {

    private final RoleService roleService;

    public GetProfessionalRolesImpl(RoleService roleService) {
        this.roleService = roleService;
    }

    public List<RoleBo> execute() {
        log.debug("No input parameters");
        List<RoleBo> result = new ArrayList<>();
        Arrays.asList(ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO, ERole.ENFERMERO_ADULTO_MAYOR)
                .forEach(eRole -> result.add(new RoleBo(eRole.getId(), roleService.getRoleDescription(eRole))));
        log.debug("Output -> {}", result);
        return result;
    }
}
