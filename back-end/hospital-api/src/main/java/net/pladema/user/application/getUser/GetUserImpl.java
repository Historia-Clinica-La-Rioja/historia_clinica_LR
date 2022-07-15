package net.pladema.user.application.getUser;

import net.pladema.user.domain.UserDataBo;
import net.pladema.user.application.port.HospitalUserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetUserImpl implements GetUser {

    private final HospitalUserStorage hospitalUserStorage;
    private final Logger logger;


    public GetUserImpl(HospitalUserStorage hospitalUserStorage) {
        this.hospitalUserStorage = hospitalUserStorage;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public UserDataBo run(Integer personId) {
        logger.debug("Input -> {}", personId);
        UserDataBo result = hospitalUserStorage.getUserDataByPersonId(personId)
                .map(userDataBo -> (hospitalUserStorage.hasPassword(userDataBo.getId())) ? userDataBo : new UserDataBo(userDataBo.getId()))
				.orElse(new UserDataBo());
        logger.debug("Output -> {}", result);
        return result;
    }
}