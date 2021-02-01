package net.pladema.user.controller.service;

import net.pladema.user.controller.dto.UserDto;

public interface UserExternalService {

    UserDto getUser(Integer userId);

}
