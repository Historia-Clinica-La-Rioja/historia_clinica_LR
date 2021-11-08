package ar.lamansys.sgx.auth.user.infrastructure.input.service;

import ar.lamansys.sgx.auth.user.application.createtokenpasswordreset.CreateTokenPasswordReset;
import ar.lamansys.sgx.auth.user.application.disableUser.DisableUser;
import ar.lamansys.sgx.auth.user.application.enableuser.EnableUser;
import ar.lamansys.sgx.auth.user.application.registeruser.RegisterUser;
import ar.lamansys.sgx.auth.user.application.updatelogindate.UpdateLoginDate;
import ar.lamansys.sgx.auth.user.application.updatepassword.UpdatePassword;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.infrastructure.input.service.dto.UserInfoDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserExternalServiceImpl implements UserExternalService {

    private final RegisterUser registerUser;

    private final UpdatePassword updatePassword;

    private final UserStorage userStorage;

    private final EnableUser enableUser;

    private final UpdateLoginDate updateLoginDate;

    private final DisableUser disableUser;

    private final CreateTokenPasswordReset createTokenPasswordReset;

    public UserExternalServiceImpl(RegisterUser registerUser,
                                   UpdatePassword updatePassword,
                                   UserStorage userStorage,
                                   EnableUser enableUser,
                                   UpdateLoginDate updateLoginDate,
                                   DisableUser disableUser,
                                   CreateTokenPasswordReset createTokenPasswordReset) {
        this.registerUser = registerUser;
        this.updatePassword = updatePassword;
        this.userStorage = userStorage;
        this.enableUser = enableUser;
        this.updateLoginDate = updateLoginDate;
        this.disableUser = disableUser;
        this.createTokenPasswordReset = createTokenPasswordReset;
    }

    @Override
    public Optional<UserInfoDto> getUser(String key) {
        try {
            UserBo userBo = userStorage.getUser(key);
            return Optional.of(mapUserDto(userBo));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserInfoDto> getUser(Integer id) {
        try {
            UserBo userBo = userStorage.getUser(id);
            return Optional.of(mapUserDto(userBo));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private UserInfoDto mapUserDto(UserBo userBo) {
        return new UserInfoDto(userBo.getId(), userBo.getUsername(), userBo.getPassword(), userBo.isEnable());
    }

    @Override
    public void registerUser(String username, String email, String password) {
        registerUser.execute(username, email, password);
    }

    @Override
    public void enableUser(String username) {
        enableUser.execute(username);
    }

    @Override
    public void updatePassword(String username, String password) {
        updatePassword.execute(username, password);
    }

    @Override
    public void updateLoginDate(String username) {
        updateLoginDate.execute(username);
    }

    @Override
    public void disableUser(String username){
        disableUser.execute(username);
    }

    @Override
    public String createTokenPasswordReset(Integer userId) {
        return createTokenPasswordReset.execute(userId);
    }
}
