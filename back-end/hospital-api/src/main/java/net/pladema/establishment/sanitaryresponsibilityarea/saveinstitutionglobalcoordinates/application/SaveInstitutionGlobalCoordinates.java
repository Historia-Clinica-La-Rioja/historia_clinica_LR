package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.application.port.SaveInstitutionGlobalCoordinatesPort;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.domain.SaveInstitutionGlobalCoordinatesBo;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveInstitutionGlobalCoordinates {

	private final SaveInstitutionGlobalCoordinatesPort saveInstitutionGlobalCoordinatesPort;

	public Integer run(SaveInstitutionGlobalCoordinatesBo saveInstitutionGlobalCoordinates) {
		log.debug("Input parameter -> saveInstitutionGlobalCoordinates {}", saveInstitutionGlobalCoordinates);
		saveInstitutionGlobalCoordinatesPort.run(saveInstitutionGlobalCoordinates);
		Integer result = 1;
		log.debug("Output -> {}", result);
		return result;
	}

}
