package ar.lamansys.sgx.auth.user.application.updateusername;

import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateUsernameImpl implements UpdateUsername {

    private final Logger logger;

    private final UserStorage userStorage;

    public UpdateUsernameImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void execute(Integer userId, String username) {
        logger.debug("Input -> userId{}, username{} ", userId, username);
        UserBo user = userStorage.getUser(userId);
        user.setUsername(username);
        userStorage.update(user);
    }
}