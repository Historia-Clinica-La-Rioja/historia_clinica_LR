package net.pladema.establishment.sanitaryresponsibilityarea.getinstitutioncoordinates.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutioncoordinates.application.FetchInstitutionGlobalCoordinates;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.GlobalCoordinatesDto;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper.GlobalCoordinatesMapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/sanitary-responsibility-area/get-institution-global-coordinates")
@RestController
public class GetInstitutionCoordinatesController {

	private final FetchInstitutionGlobalCoordinates fetchInstitutionGlobalCoordinates;

	private final GlobalCoordinatesMapper globalCoordinatesMapper;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public GlobalCoordinatesDto run(@PathVariable("institutionId") Integer institutionId) {
		log.debug("Input parameter -> institutionId {}", institutionId);
		GlobalCoordinatesBo resultBo = fetchInstitutionGlobalCoordinates.run(institutionId);
		GlobalCoordinatesDto result = globalCoordinatesMapper.toGlobalCoordinatesDto(resultBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
