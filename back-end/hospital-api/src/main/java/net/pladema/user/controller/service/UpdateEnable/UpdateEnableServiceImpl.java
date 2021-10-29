package net.pladema.user.controller.service.UpdateEnable;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import net.pladema.user.controller.service.validator.UserAuthoritiesValidator;
import net.pladema.user.controller.service.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateEnableServiceImpl implements UpdateEnableService {

    private final UserRepository userRepository;
    private final Logger logger;
    private final UserValidator userValidator;

    public UpdateEnableServiceImpl(UserRepository userRepository,
                                   UserAuthoritiesValidator userAuthoritiesValidator) {
        this.userRepository = userRepository;
        this.userValidator = new UserValidator(userAuthoritiesValidator);
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public Boolean run(Integer userId, Boolean enable) {
        logger.debug("Input -> userId {}, enable {}", userId);
        userValidator.assertUpdate(userId);
        userRepository.changeStatusAccount(userId, enable);
        logger.debug("Output -> {}", Boolean.TRUE);
        return Boolean.TRUE;
    }
}
