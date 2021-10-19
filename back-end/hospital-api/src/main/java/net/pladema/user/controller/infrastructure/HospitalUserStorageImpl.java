package net.pladema.user.controller.infrastructure;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.user.controller.service.domain.UserPersonInfoBo;
import net.pladema.user.repository.UserPersonRepository;
import net.pladema.user.service.domain.HospitalUserStorage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HospitalUserStorageImpl implements HospitalUserStorage {

    private final UserPersonRepository userPersonRepository;

    private final PersonExternalService personExternalService;

    private final UserExternalService userExternalService;

    public HospitalUserStorageImpl(UserPersonRepository userPersonRepository,
                                   PersonExternalService personExternalService,
                                   UserExternalService userExternalService) {
        this.userPersonRepository = userPersonRepository;
        this.personExternalService = personExternalService;
        this.userExternalService = userExternalService;
    }

    @Override
    public Optional<UserPersonInfoBo> getUserPersonInfo(Integer userId) {
        return userExternalService.getUser(userId)
                .map(userInfoDto -> {
                    UserPersonInfoBo result = new UserPersonInfoBo();
                    result.setId(userInfoDto.getId());
                    result.setEmail(userInfoDto.getUsername());
                    return result;
                })
                .map(this::loadPersonInfo);

    }

    private UserPersonInfoBo loadPersonInfo(UserPersonInfoBo userPersonInfoBo) {
        userPersonRepository.getByUserId(userPersonInfoBo.getId())
                .map(userPerson -> personExternalService.getBasicDataPerson(userPerson.getPersonId()))
                .ifPresent(basicDataPersonDto -> {
                    userPersonInfoBo.setPersonId(basicDataPersonDto.getId());
                    userPersonInfoBo.setFirstName(basicDataPersonDto.getFirstName());
                    userPersonInfoBo.setLastName(basicDataPersonDto.getLastName());
                });
        return userPersonInfoBo;
    }
}
