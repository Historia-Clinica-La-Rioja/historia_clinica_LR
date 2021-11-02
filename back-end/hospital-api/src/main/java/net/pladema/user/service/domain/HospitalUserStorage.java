package net.pladema.user.service.domain;

import net.pladema.user.controller.service.domain.UserPersonInfoBo;

import java.util.Optional;

public interface HospitalUserStorage {
    Optional<UserPersonInfoBo> getUserPersonInfo(Integer userId);
}
