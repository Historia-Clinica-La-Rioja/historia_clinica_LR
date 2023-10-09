package net.pladema.user.application.getusersbyroles;

import net.pladema.user.domain.PersonDataBo;

import java.util.List;

public interface FetchUsersByRoles {
	List<PersonDataBo> execute(Integer institutionId, List<Short> rolesId);
}
