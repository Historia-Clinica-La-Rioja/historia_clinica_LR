package net.pladema.user.application.updateEnable;

import net.pladema.establishment.application.port.HierarchicalUnitStorage;
import net.pladema.user.application.validator.UserAuthoritiesValidator;
import net.pladema.user.application.validator.UserValidator;
import net.pladema.user.application.port.HospitalUserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateEnableImpl implements UpdateEnable {

    private final Logger logger;
    private final UserValidator userValidator;
    private final HospitalUserStorage hospitalUserStorage;
	private final HierarchicalUnitStorage hierarchicalUnitStorage;


    public UpdateEnableImpl(UserAuthoritiesValidator userAuthoritiesValidator,
                            HospitalUserStorage hospitalUserStorage,
							HierarchicalUnitStorage hierarchicalUnitStorage) {
        this.userValidator = new UserValidator(userAuthoritiesValidator);
        this.hospitalUserStorage = hospitalUserStorage;
        this.logger = LoggerFactory.getLogger(getClass());
		this.hierarchicalUnitStorage = hierarchicalUnitStorage;
    }

    @Override
    public Boolean run(Integer userId, Boolean enable) {
        logger.debug("Input -> userId {}, enable {}", userId, enable);
        userValidator.assertUpdate(userId);
        if (enable)
            hospitalUserStorage.enableUser(userId);
        else {
			hospitalUserStorage.disableUser(userId);
			hierarchicalUnitStorage.deleteHierarchicalUnitStaff(userId);
		}
        logger.debug("Output -> {}", Boolean.TRUE);
        return Boolean.TRUE;
    }
}
