package ar.lamansys.sgx.auth.user.application.updatelogindate;

import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateLoginDateImpl implements UpdateLoginDate {

    private final Logger logger;

    private final UserStorage userStorage;

    private final DateTimeProvider dateTimeProvider;

    public UpdateLoginDateImpl(UserStorage userStorage,
                               DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.userStorage = userStorage;
    }

    @Override
    public void execute(String username) {
        logger.debug("Enable user -> {}", username);
        UserBo user = userStorage.getUser(username);
		user.setPrevoiusLogin(user.getLastLogin());
        user.setLastLogin(dateTimeProvider.nowDateTime());
		userStorage.update(user);
    }

}
