package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NominatimAddressDto {

	private String streetName;

	private String houseNumber;

	private String cityName;

	private String stateName;

	private String postalCode;

}
