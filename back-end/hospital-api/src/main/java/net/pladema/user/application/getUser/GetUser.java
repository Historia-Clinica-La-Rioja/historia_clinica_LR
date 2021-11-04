package net.pladema.user.application.getUser;

import net.pladema.user.domain.UserDataBo;

public interface GetUser {
    UserDataBo run(Integer personId);
}
