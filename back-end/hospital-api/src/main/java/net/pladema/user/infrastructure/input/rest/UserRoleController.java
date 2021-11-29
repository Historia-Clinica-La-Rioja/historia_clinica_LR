package net.pladema.user.infrastructure.input.rest;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.user.application.getrolesbyuser.GetRolesByUser;
import net.pladema.user.infrastructure.input.rest.dto.UserRoleDto;
import net.pladema.user.infrastructure.input.rest.mapper.HospitalUserRoleMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-role/institution/{institutionId}")
@Api(value = "userRole", tags = {"User Role"})
@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
public class UserRoleController {

    private final GetRolesByUser getRolesByUser;

    private final HospitalUserRoleMapper hospitalUserRoleMapper;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRoleDto>> getRolesByUser(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "userId") Integer userId){
        log.debug("Input parameters -> {}",userId);
        List<UserRoleDto> result = hospitalUserRoleMapper.toListUserRoleDto(getRolesByUser.execute(userId,institutionId));
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
