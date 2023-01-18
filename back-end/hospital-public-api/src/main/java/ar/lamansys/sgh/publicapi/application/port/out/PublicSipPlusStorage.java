package ar.lamansys.sgh.publicapi.application.port.out;

import net.minidev.json.JSONObject;

public interface PublicSipPlusStorage {

	JSONObject getMotherBasicData(String documentType, String documentNumber);

}
