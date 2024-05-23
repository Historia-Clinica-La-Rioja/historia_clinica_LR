package net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.application.port.GetSanitaryResponsibilityAreaInstitutionAddressPort;
import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.domain.GetSanitaryResponsibilityAreaInstitutionAddressBo;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetSanitaryResponsibilityAreaInstitutionAddress {

	private final GetSanitaryResponsibilityAreaInstitutionAddressPort getSanitaryResponsibilityAreaInstitutionAddressPort;

	public GetSanitaryResponsibilityAreaInstitutionAddressBo run(Integer institutionId) {
		log.debug("Input parameter -> institutionId {}", institutionId);
		GetSanitaryResponsibilityAreaInstitutionAddressBo result = getSanitaryResponsibilityAreaInstitutionAddressPort.run(institutionId);
		log.debug("Output -> {}", result);
		return result;
	}

}
