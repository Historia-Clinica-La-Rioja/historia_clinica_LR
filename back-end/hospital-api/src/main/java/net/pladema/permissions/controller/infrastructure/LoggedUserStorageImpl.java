package net.pladema.permissions.controller.infrastructure;

import net.pladema.permissions.service.LoggedUserStorage;
import net.pladema.permissions.service.domain.LoggedUserBo;
import net.pladema.user.controller.service.UserPersonExternalService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoggedUserStorageImpl implements LoggedUserStorage {

    private final UserPersonExternalService userPersonExternalService;

    public LoggedUserStorageImpl(UserPersonExternalService userPersonExternalService) {
        this.userPersonExternalService = userPersonExternalService;
    }

    @Override
    public Optional<LoggedUserBo> getUserInfo(Integer userId) {
        return userPersonExternalService.getUser(userId)
                .map(userDto -> new LoggedUserBo(userDto.getEmail(),
                        userDto.getId(),
                        userDto.getPersonId(),
                        userDto.getFirstName(),
                        userDto.getLastName()));

    }
}
