package net.pladema.authorization.application.port;

import net.pladema.authorization.domain.UserPersonaBo;
import net.pladema.authorization.application.port.exceptions.InvalidUserException;

public interface UserPersonStorage {

    UserPersonaBo fetchUserPerson(Integer userId) throws InvalidUserException;

}
