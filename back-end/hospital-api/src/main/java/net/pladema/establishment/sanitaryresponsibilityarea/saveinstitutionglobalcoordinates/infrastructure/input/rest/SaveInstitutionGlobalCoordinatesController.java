package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.application.SaveInstitutionGlobalCoordinates;
import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.domain.SaveInstitutionGlobalCoordinatesBo;
import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.infrastructure.input.mapper.SaveInstitutionGlobalCoordinatesMapper;
import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.infrastructure.input.rest.dto.SaveInstitutionGlobalCoordinatesDto;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/sanitary-responsibility-area/save-institution-global-coordinates")
@RestController
public class SaveInstitutionGlobalCoordinatesController {

	private final SaveInstitutionGlobalCoordinatesMapper saveInstitutionGlobalCoordinatesMapper;

	private final SaveInstitutionGlobalCoordinates saveInstitutionGlobalCoordinates;

	@PostMapping
	public Integer run(@PathVariable("institutionId") Integer institutionId, @RequestBody SaveInstitutionGlobalCoordinatesDto saveInstitutionGlobalCoordinatesDto) {
		log.debug("Input parameters -> institutionId {}, saveInstitutionGlobalCoordinatesDto {}", institutionId, saveInstitutionGlobalCoordinatesDto);
		SaveInstitutionGlobalCoordinatesBo saveInstitutionGlobalCoordinatesBo = saveInstitutionGlobalCoordinatesMapper.toSaveInstitutionGlobalCoordinatesBo(saveInstitutionGlobalCoordinatesDto, institutionId);
		Integer result = saveInstitutionGlobalCoordinates.run(saveInstitutionGlobalCoordinatesBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
