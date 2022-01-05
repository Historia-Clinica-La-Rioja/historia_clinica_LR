package net.pladema.user.application.hasbackofficerole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.user.application.port.UserRoleStorage;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class HasBackofficeRoleImpl implements HasBackofficeRole {

    private final UserRoleStorage userRoleStorage;

    @Override
    public Boolean execute(Integer userId) {
        log.debug("Input parameters -> {}", userId);
        Boolean result = userRoleStorage.getRolesByUser(userId).stream()
                .filter(userRoleBo ->
                        (userRoleBo.getRoleId() == ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.getId()
                                || userRoleBo.getRoleId() == ERole.ADMINISTRADOR.getId()))
                .collect(Collectors.toList()).stream().findAny().isPresent();
        log.debug("Output ->{}", result);
        return result;
    }
}
