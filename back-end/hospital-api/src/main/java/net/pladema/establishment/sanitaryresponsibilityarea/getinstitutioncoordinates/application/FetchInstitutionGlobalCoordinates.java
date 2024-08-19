package net.pladema.establishment.sanitaryresponsibilityarea.getinstitutioncoordinates.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutioncoordinates.application.port.InstitutionAddressGlobalCoordinatesPort;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchInstitutionGlobalCoordinates {

	private final InstitutionAddressGlobalCoordinatesPort institutionAddressGlobalCoordinatesPort;

	public GlobalCoordinatesBo run(Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		GlobalCoordinatesBo result = institutionAddressGlobalCoordinatesPort.fetchInstitutionGlobalCoordinates(institutionId);
		log.debug("Output -> {}", result);
		return result;
	}

}
