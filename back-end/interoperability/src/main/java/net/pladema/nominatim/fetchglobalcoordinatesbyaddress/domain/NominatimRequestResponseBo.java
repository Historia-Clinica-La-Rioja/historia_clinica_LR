package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.enums.ENominatimResponseCode;

@Getter
@Setter
@AllArgsConstructor
public class NominatimRequestResponseBo {

	private ENominatimResponseCode responseCode;

	private GlobalCoordinatesBo globalCoordinates;

}
