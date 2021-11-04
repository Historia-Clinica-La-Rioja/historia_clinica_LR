package net.pladema.user.application.createDefaultUser;

import net.pladema.user.domain.UserDataBo;
import net.pladema.user.application.port.HospitalUserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateDefaultUserImpl implements CreateDefaultUser {
    private final HospitalUserStorage hospitalUserStorage;

    private final Logger logger;

    public CreateDefaultUserImpl(HospitalUserStorage hospitalUserStorage) {
        this.hospitalUserStorage = hospitalUserStorage;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public Integer run(Integer personId) {
        logger.debug("Input -> {}", personId);
        String identificatioNumber = hospitalUserStorage.getIdentificationNumber(personId);
        String username = personId + "_" + identificatioNumber;
        hospitalUserStorage.registerUser(username, null, null);
        UserDataBo saved = hospitalUserStorage.getUserByUsername(username);
        hospitalUserStorage.saveUserPerson(saved.getId(), personId);
        Integer result = saved.getId();
        logger.debug("Output -> {}", result);
        return result;
    }
}
