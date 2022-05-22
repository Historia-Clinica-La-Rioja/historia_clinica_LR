package net.pladema.user.infrastructure.input.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.user.application.getrolesbyuser.GetRolesByUser;
import net.pladema.user.application.hasbackofficerole.HasBackofficeRole;
import net.pladema.user.application.updateuserrole.UpdateUserRole;
import net.pladema.user.infrastructure.input.rest.dto.UserRoleDto;
import net.pladema.user.infrastructure.input.rest.mapper.HospitalUserRoleMapper;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-role/institution/{institutionId}/user/{userId}")
@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
public class UserRoleController {

    private final GetRolesByUser getRolesByUser;

    private final HospitalUserRoleMapper hospitalUserRoleMapper;

    private final UpdateUserRole updateUserRole;

    private final HasBackofficeRole hasBackofficeRole;

    @GetMapping()
    public ResponseEntity<List<UserRoleDto>> getRolesByUser(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "userId") Integer userId){
        log.debug("Input parameters -> userId={}", userId);
        List<UserRoleDto> result = hospitalUserRoleMapper.toListUserRoleDto(getRolesByUser.execute(userId,institutionId));
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping()
    public void updateRoles(@PathVariable(name = "institutionId") Integer institutionId,
                            @PathVariable(name = "userId") Integer userId,
                            @RequestBody List<UserRoleDto> userRoleDtos) {
        log.debug("Input parameters -> userRoles={}", userRoleDtos);
        updateUserRole.execute(hospitalUserRoleMapper.toListUserRoleBo(userRoleDtos), userId, institutionId);
    }

    @GetMapping("/has-backoffice-role")
    public ResponseEntity<Boolean> hasBackofficeRole(
			@PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "userId") Integer userId){
        log.debug("Input parameters -> institutionId {}, userId {}",institutionId,userId);
        Boolean result = hasBackofficeRole.execute(userId);
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
