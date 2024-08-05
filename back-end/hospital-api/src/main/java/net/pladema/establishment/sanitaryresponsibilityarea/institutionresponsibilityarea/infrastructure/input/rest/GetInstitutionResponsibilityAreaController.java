package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.application.getinstitutionresponsibilityarea.GetInstitutionResponsibilityArea;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.GlobalCoordinatesDto;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper.GlobalCoordinatesMapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/sanitary-responsibility-area/get-institution-responsibility-area")
@RestController
public class GetInstitutionResponsibilityAreaController {

	private final GlobalCoordinatesMapper globalCoordinatesMapper;

	private final GetInstitutionResponsibilityArea getInstitutionResponsibilityArea;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public List<GlobalCoordinatesDto> run(@PathVariable Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<GlobalCoordinatesBo> resultBo = getInstitutionResponsibilityArea.run(institutionId);
		List<GlobalCoordinatesDto> result = globalCoordinatesMapper.toGlobalCoordinatesDtoList(resultBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
