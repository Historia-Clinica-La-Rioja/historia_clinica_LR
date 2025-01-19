package net.pladema.establishment.sanitaryresponsibilityarea.getcoordinatesfromaddress.infrastructure.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.getcoordinatesfromaddress.application.GetCoordinatesFromAddress;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.GlobalCoordinatesDto;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.NominatimAddressDto;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper.GlobalCoordinatesMapper;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper.NominatimAddressMapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/sanitary-responsibility-area/get-global-coordinates-by-address")
@RestController
public class GetCoordinatesFromAddressController {

	private final ObjectMapper objectMapper;

	private final NominatimAddressMapper nominatimAddressMapper;

	private final GlobalCoordinatesMapper globalCoordinatesMapper;

	private final GetCoordinatesFromAddress getCoordinatesFromAddress;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public GlobalCoordinatesDto run(@RequestParam("address") String address) throws JsonProcessingException {
		log.debug("Input parameters -> address {}", address);
		NominatimAddressDto addressDto = objectMapper.readValue(address, NominatimAddressDto.class);
		NominatimAddressBo globalCoordinatesBo = nominatimAddressMapper.fromNominatimAddressDto(addressDto);
		GlobalCoordinatesBo resultBo = getCoordinatesFromAddress.run(globalCoordinatesBo);
		GlobalCoordinatesDto result = globalCoordinatesMapper.toGlobalCoordinatesDto(resultBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
