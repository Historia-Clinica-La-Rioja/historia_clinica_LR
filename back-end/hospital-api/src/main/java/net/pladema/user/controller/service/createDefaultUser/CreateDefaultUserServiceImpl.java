package net.pladema.user.controller.service.createDefaultUser;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import ar.lamansys.sgx.auth.user.infrastructure.input.service.dto.UserInfoDto;
import net.pladema.person.repository.PersonRepository;
import net.pladema.user.controller.service.createDefaultUser.exceptions.CreateDefaultUserException;
import net.pladema.user.controller.service.createDefaultUser.exceptions.CreateDefaultUserExceptionEnum;
import net.pladema.user.controller.service.domain.UserDataBo;
import net.pladema.user.repository.UserPersonRepository;
import net.pladema.user.repository.entity.UserPerson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateDefaultUserServiceImpl implements CreateDefaultUserService {
    private final UserExternalService userExternalService;
    private final UserPersonRepository userPersonRepository;
    private final PersonRepository personRepository;
    private final Logger logger;

    public CreateDefaultUserServiceImpl(UserExternalService userExternalService,
                                        UserPersonRepository userPersonRepository,
                                        PersonRepository personRepository) {
        this.userExternalService = userExternalService;
        this.userPersonRepository = userPersonRepository;
        this.personRepository = personRepository;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public Integer run(Integer personId) {
        logger.debug("Input -> {}", personId);
        String identificatioNumber = personRepository.getCompletePerson(personId).get().getPerson().getIdentificationNumber();
        String username = personId + "_" + identificatioNumber;
        userExternalService.registerUser(username, null, null);
        UserDataBo saved = mapUserBo(userExternalService.getUser(username)
                .orElseThrow(() -> new CreateDefaultUserException(CreateDefaultUserExceptionEnum.UNEXISTED_USER, "El usuario %s no existe")));
        userPersonRepository.save(new UserPerson(saved.getId(), personId));
        Integer result = saved.getId();
        logger.debug("Output -> {}", result);
        return result;
    }

    private UserDataBo mapUserBo(UserInfoDto user) {
        return new UserDataBo(user.getId(), user.getUsername(), user.isEnabled());
    }
}
