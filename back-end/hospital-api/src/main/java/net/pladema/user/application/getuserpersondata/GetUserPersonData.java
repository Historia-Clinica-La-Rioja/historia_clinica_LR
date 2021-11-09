package net.pladema.user.application.getuserpersondata;

import net.pladema.user.domain.PersonDataBo;

public interface GetUserPersonData {

    PersonDataBo execute(String token);
}
