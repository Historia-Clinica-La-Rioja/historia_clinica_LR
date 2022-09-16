package ar.lamansys.sgx.auth.jwt.infrastructure.output.user;

import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoStorage;
import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import ar.lamansys.sgx.auth.user.infrastructure.input.service.dto.UserInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserInfoStorageImpl implements UserInfoStorage {

    private final Logger logger;

    private final UserExternalService userExternalService;

    public UserInfoStorageImpl(UserExternalService userExternalService) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.userExternalService = userExternalService;
    }

    @Override
    public UserInfoBo getUser(String username) {
        return userExternalService.getUser(username)
                .map(this::toUserInfoBo)
                .orElse(null);
    }

	@Override
	public UserInfoBo getUser(Integer userId) {
		return userExternalService.getUser(userId)
				.map(this::toUserInfoBo)
				.orElse(null);
	}

	@Override
    public void updateLoginDate(String username) {
        logger.debug("Update login date {}",username);
        userExternalService.updateLoginDate(username);
    }

    @Override
    public Boolean fetchUserHasTwoFactorAuthenticationEnabled(Integer userId) {
        return userExternalService.fetchUserHasTwoFactorAuthenticationEnabled(userId);
    }

    private UserInfoBo toUserInfoBo(UserInfoDto userInfoDto) {
        return new UserInfoBo(userInfoDto.getId(),
                userInfoDto.getUsername(),
                userInfoDto.isEnabled(),
                userInfoDto.getPassword());
    }
}
