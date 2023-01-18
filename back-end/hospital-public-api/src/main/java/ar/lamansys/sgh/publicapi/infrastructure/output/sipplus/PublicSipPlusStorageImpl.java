package ar.lamansys.sgh.publicapi.infrastructure.output.sipplus;

import ar.lamansys.sgh.publicapi.application.port.out.PublicSipPlusStorage;
import ar.lamansys.sgh.publicapi.application.port.out.exceptions.SipPlusException;
import ar.lamansys.sgh.publicapi.application.port.out.exceptions.SipPlusExceptionEnum;
import ar.lamansys.sgh.publicapi.domain.sipplus.EmbeddedAuthenticationDataBo;
import ar.lamansys.sgh.publicapi.domain.sipplus.SipPlusCodes;
import ar.lamansys.sgh.shared.infrastructure.input.service.PersonInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.minidev.json.JSONObject;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicSipPlusStorageImpl implements PublicSipPlusStorage {

	private final SharedPersonPort sharedPersonPort;

	@Override
	public JSONObject getMotherBasicData(String documentType, String documentNumber) {
		log.debug("Input parameters -> documentType {}, documentNumber {}", documentType, documentNumber);

		PersonInfoDto motherInfo = sharedPersonPort.getPersonByIdentificationInfo(documentType, documentNumber);

		String lastNames = Stream.of(motherInfo.getLastName(), motherInfo.getOtherLastNames())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));

		String names = Stream.of(motherInfo.getFirstName(), motherInfo.getMiddleNames())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));

		JSONObject result = new JSONObject();
		result.put(SipPlusCodes.NAME, names);
		result.put(SipPlusCodes.LAST_NAME, lastNames);
		result.put(SipPlusCodes.BIRTH_DATE, motherInfo.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
		result.put(SipPlusCodes.DOCUMENT_TYPE, motherInfo.getIdentificationTypeDescription());
		result.put(SipPlusCodes.DOCUMENT_NUMBER, motherInfo.getIdentificationNumber());

		log.debug("Get basic mother information", result.toJSONString());
		return result;
	}

}
