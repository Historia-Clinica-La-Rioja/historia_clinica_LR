package net.pladema.cipres.application.port;

import net.pladema.cipres.domain.PersonDataBo;

public interface CipresPersonStorage {

	PersonDataBo getPersonData(Integer personId);

}
