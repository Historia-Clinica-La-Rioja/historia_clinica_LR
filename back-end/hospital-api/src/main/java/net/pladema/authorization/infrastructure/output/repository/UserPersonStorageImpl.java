package net.pladema.authorization.infrastructure.output.repository;

import org.springframework.stereotype.Service;

import net.pladema.authorization.application.port.UserPersonStorage;
import net.pladema.authorization.application.port.exceptions.InvalidUserException;
import net.pladema.authorization.application.port.exceptions.InvalidUserExceptionEnum;
import net.pladema.authorization.domain.UserPersonaBo;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.user.controller.service.UserPersonExternalService;

@Service
public class UserPersonStorageImpl implements UserPersonStorage {

    private final UserPersonExternalService userPersonExternalService;
	private final PersonExternalService personExternalService;

    public UserPersonStorageImpl(
			UserPersonExternalService userPersonExternalService,
			PersonExternalService personExternalService
	) {
        this.userPersonExternalService = userPersonExternalService;
		this.personExternalService = personExternalService;
    }

    @Override
    public UserPersonaBo fetchUserPerson(Integer userId) throws InvalidUserException {
        return userPersonExternalService.getUser(userId)
                .map(userDto -> new UserPersonaBo(
						userDto.getEmail(),
                        userDto.getId(),
                        userDto.getPersonId(),
                        userDto.getFirstName(),
                        userDto.getLastName(),
						() -> personExternalService.getPersonPhoto(userDto.getPersonId()).getImageData(),
						userDto.getNameSelfDetermination(),
						userDto.getPreviousLogin()
				))
				.orElseThrow(() -> new InvalidUserException(InvalidUserExceptionEnum.INVALID_USER, userId));
    }
}
