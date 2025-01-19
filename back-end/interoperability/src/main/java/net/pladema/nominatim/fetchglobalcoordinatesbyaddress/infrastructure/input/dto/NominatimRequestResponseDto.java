package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.enums.ENominatimResponseCode;

@Getter
@Setter
@NoArgsConstructor
public class NominatimRequestResponseDto {

	private ENominatimResponseCode responseCode;

	private GlobalCoordinatesDto globalCoordinates;

}
