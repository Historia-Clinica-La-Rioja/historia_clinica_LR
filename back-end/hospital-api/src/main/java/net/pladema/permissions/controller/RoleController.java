package net.pladema.permissions.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.application.getinstitutionalroles.GetInstitutionalRoles;
import net.pladema.permissions.controller.dto.RoleDto;
import net.pladema.permissions.controller.mappers.RoleDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@Api(value = "roles", tags = {"Roles"})
public class RoleController {

    private final GetInstitutionalRoles getInstitutionalRoles;

    private final RoleDtoMapper roleDtoMapper;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllInstitutionalRoles() {
        log.debug("No input parameters");
        List<RoleDto> result = roleDtoMapper.toListRoleDto(getInstitutionalRoles.execute());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
