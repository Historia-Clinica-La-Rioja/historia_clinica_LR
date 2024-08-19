package net.pladema.establishment.sanitaryresponsibilityarea.getinstitutioncoordinates.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.address.repository.AddressRepository;
import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutioncoordinates.application.port.InstitutionAddressGlobalCoordinatesPort;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InstitutionAddressGlobalCoordinatesPortImpl implements InstitutionAddressGlobalCoordinatesPort {

	private final AddressRepository addressRepository;

	@Override
	public GlobalCoordinatesBo fetchInstitutionGlobalCoordinates(Integer institutionId) {
		return addressRepository.fetchInstitutionGlobalCoordinates(institutionId);
	}

}
