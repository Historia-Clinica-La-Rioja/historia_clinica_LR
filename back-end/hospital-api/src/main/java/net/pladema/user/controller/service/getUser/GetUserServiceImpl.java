package net.pladema.user.controller.service.getUser;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import net.pladema.user.controller.service.domain.UserDataBo;
import net.pladema.user.repository.UserPersonRepository;
import net.pladema.user.repository.VHospitalUserRepository;
import net.pladema.user.repository.entity.VHospitalUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUserServiceImpl implements GetUserService {

    private final VHospitalUserRepository vHospitalUserRepository;
    private final UserExternalService userExternalService;
    private final UserPersonRepository userPersonRepository;
    private final Logger logger;


    public GetUserServiceImpl(VHospitalUserRepository vHospitalUserRepository,
                              UserPersonRepository userPersonRepository,
                              UserExternalService userExternalService) {
        this.vHospitalUserRepository = vHospitalUserRepository;
        this.userPersonRepository = userPersonRepository;
        this.userExternalService = userExternalService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public UserDataBo run(Integer personId) {
        logger.debug("Input -> {}", personId);
        UserDataBo result = new UserDataBo();
        Optional<Integer> userId = userPersonRepository.getUserIdByPersonId(personId);
        if (userId.isPresent()) {
            Boolean hasPassword = userExternalService.getUser(userId.get()).get().getPassword() != null;
            VHospitalUser user = vHospitalUserRepository.findById(userId.get()).get();
            result = hasPassword ? mapUserBo(user) : new UserDataBo(userId.get());
        }
        logger.debug("Output -> {}", result);
        return result;
    }

    private UserDataBo mapUserBo(VHospitalUser vHospitalUser) {
        return new UserDataBo(vHospitalUser.getUserId(), vHospitalUser.getUsername(), vHospitalUser.getEnable(), vHospitalUser.getLastLogin());
    }
}