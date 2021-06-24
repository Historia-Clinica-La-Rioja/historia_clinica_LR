package net.pladema.permissions.controller.infrastructure;

import net.pladema.permissions.service.LoggedUserStorage;
import net.pladema.permissions.service.domain.LoggedUserBo;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.user.controller.service.UserPersonExternalService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoggedUserStorageImpl implements LoggedUserStorage {

    private final PersonExternalService personExternalService;

    private final UserPersonExternalService userPersonExternalService;

    public LoggedUserStorageImpl(PersonExternalService personExternalService,
                                 UserPersonExternalService userPersonExternalService) {
        this.personExternalService = personExternalService;
        this.userPersonExternalService = userPersonExternalService;
    }

    @Override
    public Optional<LoggedUserBo> getUserInfo(Integer userId) {
        return userPersonExternalService.getUser(userId)
                .map(userDto -> personExternalService.findBasicDataPerson(userDto.getPersonId())
                        .map(basicDataPersonDto ->
                                new LoggedUserBo(userDto.getEmail(), userDto.getId(), basicDataPersonDto.getId(),
                                        basicDataPersonDto.getFirstName(), basicDataPersonDto.getLastName()))
                        .get());
    }
}
