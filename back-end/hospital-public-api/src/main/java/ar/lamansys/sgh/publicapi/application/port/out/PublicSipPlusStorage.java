package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.sipplus.EmbeddedAuthenticationDataBo;

public interface PublicSipPlusStorage {
	EmbeddedAuthenticationDataBo getDataForAuthentication(String accessData);

}
