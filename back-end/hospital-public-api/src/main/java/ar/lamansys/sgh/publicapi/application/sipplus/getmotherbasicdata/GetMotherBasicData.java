package ar.lamansys.sgh.publicapi.application.sipplus.getmotherbasicdata;

import ar.lamansys.sgh.publicapi.application.port.out.PublicSipPlusStorage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetMotherBasicData {

	private final PublicSipPlusStorage publicSipPlusStorage;

	public JSONObject run(String documentType, String documentNumber) {
		log.debug("Input parameters -> documentType {}, documentNumber {} ", documentType, documentNumber);
		return publicSipPlusStorage.getMotherBasicData(documentType, documentNumber);
	}

}
