package net.pladema.user.controller.service.getUser;

import net.pladema.user.controller.service.domain.UserDataBo;

public interface GetUserService {
    UserDataBo run(Integer personId);
}
