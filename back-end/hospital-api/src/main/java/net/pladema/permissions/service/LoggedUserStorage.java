package net.pladema.permissions.service;

import net.pladema.permissions.service.domain.LoggedUserBo;

import java.util.Optional;

public interface LoggedUserStorage {

    Optional<LoggedUserBo> getUserInfo(Integer userId);
}
