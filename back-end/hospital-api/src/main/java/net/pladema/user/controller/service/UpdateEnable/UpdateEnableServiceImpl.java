package net.pladema.user.controller.service.UpdateEnable;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateEnableServiceImpl implements UpdateEnableService {

    private final UserRepository userRepository;
    private final Logger logger;

    public UpdateEnableServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public Boolean run(Integer userId, Boolean enable) {
        logger.debug("Input -> userId {}, enable {}", userId);
        userRepository.changeStatusAccount(userId, enable);
        logger.debug("Output -> {}", Boolean.TRUE);
        return Boolean.TRUE;
    }
}
