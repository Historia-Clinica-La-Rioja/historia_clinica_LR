package ar.lamansys.sgh.publicapi.sipplus.application.port.out;

import ar.lamansys.sgh.publicapi.sipplus.domain.EmbeddedAuthenticationDataBo;

public interface PublicSipPlusStorage {
	EmbeddedAuthenticationDataBo getDataForAuthentication(String accessData);

}
