package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.address.repository.AddressRepository;
import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.application.port.SaveInstitutionAddressPort;
import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.domain.SaveInstitutionAddressBo;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaveInstitutionAddressPortImpl implements SaveInstitutionAddressPort {

	private final AddressRepository addressRepository;

	@Override
	public void run(SaveInstitutionAddressBo institutionAddressAndGlobalCoordinates) {
		addressRepository.saveInstitutionAddress(institutionAddressAndGlobalCoordinates.getInstitutionId(), institutionAddressAndGlobalCoordinates.getStateId(),
				institutionAddressAndGlobalCoordinates.getDepartmentId(), institutionAddressAndGlobalCoordinates.getCityId(),
				institutionAddressAndGlobalCoordinates.getStreetName(), institutionAddressAndGlobalCoordinates.getHouseNumber());
	}

}
