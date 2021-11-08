package net.pladema.user.application.port;

import net.pladema.user.controller.service.domain.UserPersonInfoBo;
import net.pladema.user.domain.UserDataBo;

import java.util.Optional;

public interface HospitalUserStorage {
    Optional<UserPersonInfoBo> getUserPersonInfo(Integer userId);
    Optional<UserDataBo> getUserDataByPersonId(Integer personId);
    String getIdentificationNumber(Integer personId);
    void registerUser(String username, String email, String password);
    UserDataBo getUserByUsername(String username);
    void saveUserPerson(Integer userId, Integer personId);
    void enableUser(Integer userId);
    void disableUser(Integer userId);
    Boolean hasPassword(Integer userId);
    String createTokenPasswordReset(Integer userId);
}
