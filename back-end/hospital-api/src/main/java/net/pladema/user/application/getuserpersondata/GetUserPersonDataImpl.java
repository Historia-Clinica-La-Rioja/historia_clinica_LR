package net.pladema.user.application.getuserpersondata;

import net.pladema.user.application.port.HospitalUserStorage;
import net.pladema.user.domain.PersonDataBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetUserPersonDataImpl implements GetUserPersonData {

    private final Logger logger;

    private final HospitalUserStorage hospitalUserStorage;

    public GetUserPersonDataImpl(HospitalUserStorage hospitalUserStorage) {
        this.hospitalUserStorage = hospitalUserStorage;
        this.logger = LoggerFactory.getLogger(getClass());
    }
    
    @Override
    public PersonDataBo execute(String token) {
        Integer userId = hospitalUserStorage.getUserIdByToken(token);
        PersonDataBo result = hospitalUserStorage.getPersonDataBoByUserId(userId);
        if(!(hospitalUserStorage.hasPassword(userId)))
            result.setUsername(null);
        logger.debug("Output -> {}", result);
        return result;
    }
}
