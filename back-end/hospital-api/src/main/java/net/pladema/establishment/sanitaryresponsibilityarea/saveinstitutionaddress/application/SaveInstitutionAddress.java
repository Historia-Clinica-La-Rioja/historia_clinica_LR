package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.application.port.SaveInstitutionAddressPort;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.domain.SaveInstitutionAddressBo;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveInstitutionAddress {

	private final SaveInstitutionAddressPort saveInstitutionAddressPort;

	public Integer run(SaveInstitutionAddressBo institutionAddressAndGlobalCoordinates) {
		log.debug("Input parameters -> institutionAddressAndGlobalCoordinates {}", institutionAddressAndGlobalCoordinates);
		saveInstitutionAddressPort.run(institutionAddressAndGlobalCoordinates);
		Integer result = 1;
		log.debug("Output -> {}", result);
		return result;
	}

}
