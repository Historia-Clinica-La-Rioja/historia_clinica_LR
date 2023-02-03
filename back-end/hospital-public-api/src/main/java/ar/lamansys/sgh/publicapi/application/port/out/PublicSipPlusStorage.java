package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.sipplus.EmbeddedAuthenticationDataBo;
import net.minidev.json.JSONObject;

public interface PublicSipPlusStorage {
	EmbeddedAuthenticationDataBo getDataForAuthentication(String accessData);

}
