package net.pladema.user.controller.infrastructure;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.user.controller.service.domain.UserPersonInfoBo;
import net.pladema.user.repository.UserPersonRepository;
import net.pladema.user.repository.entity.UserPerson;
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
        return userPersonRepository.getByUserId(userId)
            .map(this::apply);
    }

    private UserPersonInfoBo apply(UserPerson userPerson) {
        UserPersonInfoBo result = new UserPersonInfoBo();
        var personData = personExternalService.getBasicDataPerson(userPerson.getPersonId());
        if (personData != null) {
            result.setPersonId(personData.getId());
            result.setFirstName(personData.getFirstName());
            result.setLastName(personData.getLastName());
        }
        userExternalService.getUser(userPerson.getUserId())
                .ifPresent(userInfoDto -> {
                    result.setId(userInfoDto.getId());
                    result.setEmail(userInfoDto.getUsername());
                });
        return result;
    }
}
