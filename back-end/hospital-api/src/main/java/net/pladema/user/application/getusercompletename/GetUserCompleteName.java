package net.pladema.user.application.getusercompletename;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.person.service.PersonService;
import net.pladema.user.application.getuserpersoninfo.GetUserPersonInfo;
import net.pladema.user.controller.service.domain.UserPersonInfoBo;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetUserCompleteName {

    private final GetUserPersonInfo getUserPersonInfo;
    private final PersonService personService;

    public String run(Integer userId) {
        log.debug("Input parameters -> userId {}", userId);
        UserPersonInfoBo personInfoBo = getUserPersonInfo.run(userId);
        String result = personService.parseCompletePersonName(personInfoBo.getFirstName(),
                personInfoBo.getMiddleNames(),
                personInfoBo.getLastName(),
                personInfoBo.getOtherLastNames(),
                personInfoBo.getNameSelfDetermination());

        log.debug("Ouput -> {}", result);
        return result;
    }
}
