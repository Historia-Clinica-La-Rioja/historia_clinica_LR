package net.pladema.user.application.createTokenPassword;

import net.pladema.user.application.port.HospitalUserStorage;
import net.pladema.user.application.validator.UserAuthoritiesValidator;
import net.pladema.user.application.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateTokenPasswordImpl implements CreateTokenPassword {
    private final Logger logger;
    private final HospitalUserStorage hospitalUserStorage;
    private final UserValidator userValidator;

    public CreateTokenPasswordImpl(HospitalUserStorage hospitalUserStorage,
                                   UserAuthoritiesValidator userAuthoritiesValidator) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.hospitalUserStorage = hospitalUserStorage;
        this.userValidator = new UserValidator(userAuthoritiesValidator);
    }

    @Override
    public String run(Integer userId) {
        logger.debug("Input -> userId {}", userId);
        userValidator.assertUpdate(userId);
        String result = hospitalUserStorage.createTokenPasswordReset(userId);
        logger.debug("Output -> result {}", result);
        return result;
    }
}