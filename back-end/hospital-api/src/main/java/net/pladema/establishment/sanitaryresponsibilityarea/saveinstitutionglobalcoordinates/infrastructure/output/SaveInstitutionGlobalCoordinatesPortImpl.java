package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.address.repository.AddressRepository;
import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.application.port.SaveInstitutionGlobalCoordinatesPort;
import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.domain.SaveInstitutionGlobalCoordinatesBo;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaveInstitutionGlobalCoordinatesPortImpl implements SaveInstitutionGlobalCoordinatesPort {

	private final AddressRepository addressRepository;

	@Override
	public void run(SaveInstitutionGlobalCoordinatesBo saveInstitutionGlobalCoordinates) {
		addressRepository.saveInstitutionGlobalCoordinates(saveInstitutionGlobalCoordinates.getInstitutionId(), saveInstitutionGlobalCoordinates.getLatitude(), saveInstitutionGlobalCoordinates.getLongitude());
	}

}
