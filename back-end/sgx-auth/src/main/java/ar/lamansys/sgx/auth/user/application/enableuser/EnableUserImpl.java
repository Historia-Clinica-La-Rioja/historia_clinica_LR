package ar.lamansys.sgx.auth.user.application.enableuser;

import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EnableUserImpl implements EnableUser {

    private final Logger logger;

    private final UserStorage userStorage;

    public EnableUserImpl(UserStorage userStorage) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.userStorage = userStorage;
    }

    @Override
    public void execute(String username) {
        logger.debug("Enable user -> {}", username);
        UserBo user = userStorage.getUser(username);
        user.setEnable(true);
        userStorage.update(user);
    }

}
