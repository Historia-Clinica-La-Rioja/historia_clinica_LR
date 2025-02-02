package net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.infrastructure.output.repository;

import lombok.RequiredArgsConstructor;
import net.pladema.address.repository.AddressRepository;
import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.application.port.GetSanitaryResponsibilityAreaInstitutionAddressPort;

import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.domain.GetSanitaryResponsibilityAreaInstitutionAddressBo;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetSanitaryResponsibilityAreaInstitutionAddressPortImpl implements GetSanitaryResponsibilityAreaInstitutionAddressPort {

	private final AddressRepository addressRepository;

	@Override
	public GetSanitaryResponsibilityAreaInstitutionAddressBo run(Integer institutionId) {
		return addressRepository.fetchGetSanitaryResponsibilityAreaInstitutionAddressPort(institutionId);
	}

}
